package com.librato.metrics.client;

import com.librato.metrics.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * The main class that should be used to access the Librato API
 */
public class LibratoClient {
    private static final String LIB_VERSION = Versions.getVersion("META-INF/maven/com.librato.metrics/librato-java/pom.properties", LibratoBatch.class);
    private final URI uri;
    private final int batchSize;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final IPoster poster;
    private final ExecutorService executor;
    private final SDResponseConverter sdResponseConverter = new SDResponseConverter();
    private final MDResponseConverter mdResponseConverter = new MDResponseConverter();
    private final Map<String, String> measurementPostHeaders = new HashMap<String, String>();

    public static LibratoClientBuilder builder(String email, String token) {
        return new LibratoClientBuilder(email, token);
    }

    LibratoClient(LibratoClientAttributes attrs) {
        this.uri = URIs.removePath(attrs.uri);
        this.batchSize = attrs.batchSize;
        this.connectTimeout = attrs.connectTimeout;
        this.readTimeout = attrs.readTimeout;
        this.poster = attrs.poster;
        BlockingQueue<Runnable> queue = new SynchronousQueue<Runnable>();
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("librato-client");
                thread.setDaemon(true);
                return thread;
            }
        };
        this.executor = new ThreadPoolExecutor(0, attrs.maxInflightRequests, 10, TimeUnit.SECONDS, queue, threadFactory, new CallerRunsPolicy());
        measurementPostHeaders.put("Content-Type", "application/json");
        measurementPostHeaders.put("Authorization", Authorization.buildAuthHeader(attrs.email, attrs.token));
        measurementPostHeaders.put("User-Agent", String.format("%s librato-java/%s", attrs.agentIdentifier, LIB_VERSION));
    }

    public PostMeasuresResult postMeasures(Measures measures) {
        PostMeasuresResult result = new PostMeasuresResult();
        if (measures.isEmpty()) {
            return result;
        }
        Future<List<PostResult>> sdFuture = null;
        final Measures sdMeasures = measures.toSD();
        Measures mdMeasures = measures.toMD();
        if (!sdMeasures.isEmpty()) {
            sdFuture = postMeasures("/v1/metrics", sdMeasures, sdResponseConverter, new IBuildsPayload() {
                @Override
                public byte[] build(Measures measures) {
                    return buildSDPayload(measures);
                }
            });
        }
        Future<List<PostResult>> mdFuture = null;
        if (!mdMeasures.isEmpty()) {
            mdFuture = postMeasures("/v1/measurements", mdMeasures, mdResponseConverter, new IBuildsPayload() {
                @Override
                public byte[] build(Measures measures) {
                    return buildMDPayload(measures);
                }
            });
        }
        if (sdFuture != null) {
            result.results.addAll(Futures.get(sdFuture));
        }
        if (mdFuture != null) {
            result.results.addAll(Futures.get(mdFuture));
        }
        return result;
    }

    private Future<List<PostResult>> postMeasures(final String uri,
                                                  final Measures measures,
                                                  final IResponseConverter responseConverter,
                                                  final IBuildsPayload payloadBuilder) {
        return executor.submit(new Callable<List<PostResult>>() {
            @Override
            public List<PostResult> call() throws Exception {
                List<PostResult> results = new LinkedList<PostResult>();
                for (Measures batch : measures.partition(LibratoClient.this.batchSize)) {
                    byte[] payload = payloadBuilder.build(batch);
                    try {
                        HttpResponse response = poster.post(fullUrl(uri), connectTimeout, readTimeout, measurementPostHeaders, payload);
                        results.add(responseConverter.convert(payload, response));
                    } catch (Exception e) {
                        results.add(responseConverter.convert(payload, e));
                    }
                }
                return results;
            }
        });
    }

    private byte[] buildSDPayload(Measures measures) {
        final Map<String, Object> payload = new HashMap<String, Object>();
        Maps.putIfNotNull(payload, "measure_time", measures.getEpoch());
        Maps.putIfNotNull(payload, "source", Sanitizer.LAST_PASS.apply(measures.getSource()));
        List<Map<String, Object>> gauges = new LinkedList<Map<String, Object>>();
        List<Map<String, Object>> counters = new LinkedList<Map<String, Object>>();
        for (IMeasure measure : measures.getMeasures()) {
            Map<String, Object> measureMap = measure.toMap();
            if (measure.isGauge()) {
                gauges.add(measureMap);
            } else {
                counters.add(measureMap);
            }
        }
        Maps.putIfNotEmpty(payload, "gauges", gauges);
        Maps.putIfNotEmpty(payload, "counters", counters);
        return Json.serialize(payload);
    }

    private byte[] buildMDPayload(Measures measures) {
        final Map<String, Object> payload = new HashMap<String, Object>();
        Maps.putIfNotNull(payload, "time", measures.getEpoch());
        List<Map<String, Object>> gauges = new LinkedList<Map<String, Object>>();
        for (IMeasure measure : measures.getMeasures()) {
            Map<String, Object> measureMap = measure.toMap();
            gauges.add(measureMap);
        }
        Maps.putIfNotEmpty(payload, "measurements", gauges);
        return Json.serialize(payload);
    }

    private String fullUrl(String path) {
        return this.uri.toString() + path;
    }
}
