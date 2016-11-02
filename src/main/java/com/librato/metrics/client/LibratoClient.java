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
    private final URI sdURI;
    private final URI mdURI;
    private final int batchSize;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final IPoster poster;
    private final ExecutorService executor;
    private final Map<String, String> measurementPostHeaders = new HashMap<String, String>();

    public static LibratoClientBuilder builder(String email, String token) {
        return new LibratoClientBuilder(email, token);
    }

    LibratoClient(LibratoClientAttributes attrs) {
        this.sdURI = attrs.metricURI;
        this.mdURI = attrs.taggedURI;
        this.batchSize = attrs.batchSize;
        this.connectTimeout = attrs.connectTimeout;
        this.readTimeout = attrs.readTimeout;
        this.poster = attrs.poster;
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(attrs.maxInflightRequests);
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("librato-client");
                thread.setDaemon(true);
                return thread;
            }
        };
        this.executor = new ThreadPoolExecutor(0, 2, 60, TimeUnit.SECONDS, queue, threadFactory, new CallerRunsPolicy());
        measurementPostHeaders.put("Content-Type", "application/json");
        measurementPostHeaders.put("Authorization", Authorization.buildAuthHeader(attrs.email, attrs.token));
        measurementPostHeaders.put("User-Agent", LIB_VERSION);
    }

    public PostMeasuresResult postMeasures(Measures measures) {
        PostMeasuresResult result = new PostMeasuresResult();
        if (measures.isEmpty()) {
            return result;
        }
        List<IMeasure> sdMeasures = new LinkedList<IMeasure>();
        List<IMeasure> mdMeasures = new LinkedList<IMeasure>();
        for (IMeasure measure : measures.getMeasures()) {
            if (measure.isTagged()) {
                mdMeasures.add(measure);
            } else {
                sdMeasures.add(measure);
            }
        }
        Long epoch = measures.getEpoch();
        Future<List<PostResult>> sdFuture = null;
        if (!sdMeasures.isEmpty()) {
            sdFuture = postMeasures(sdURI.toString() + "/v1/metrics", epoch, sdMeasures, new IBuildsPayload() {
                @Override
                public byte[] build(Long epoch, List<IMeasure> batch) {
                    return buildSDPayload(epoch, batch);
                }
            });
        }
        Future<List<PostResult>> mdFuture = null;
        if (!mdMeasures.isEmpty()) {
            mdFuture = postMeasures(mdURI.toString() + "/v1/measurements", epoch, mdMeasures, new IBuildsPayload() {
                @Override
                public byte[] build(Long epoch, List<IMeasure> batch) {
                    return buildMDPayload(epoch, batch);
                }
            });
        }
        if (sdFuture != null) {
            result.sdResults.addAll(Futures.get(sdFuture));
        }
        if (mdFuture != null) {
            result.mdResults.addAll(Futures.get(mdFuture));
        }
        return result;
    }

    private Future<List<PostResult>> postMeasures(final String uri,
                                                  final Long epoch,
                                                  final List<IMeasure> measures,
                                                  final IBuildsPayload payloadBuilder) {
        return executor.submit(new Callable<List<PostResult>>() {
            @Override
            public List<PostResult> call() throws Exception {
                List<PostResult> results = new LinkedList<PostResult>();
                for (List<IMeasure> batch : Lists.partition(measures, LibratoClient.this.batchSize)) {
                    byte[] payload = payloadBuilder.build(epoch, batch);
                    try {
                        HttpResponse response = poster.post(uri, connectTimeout, readTimeout, measurementPostHeaders, payload);
                        results.add(new PostResult(payload, response));
                    } catch (Exception e) {
                        results.add(new PostResult(payload, e));
                    }
                }
                return results;
            }
        });
    }

    private byte[] buildSDPayload(Long epoch, List<IMeasure> measures) {
        final Map<String, Object> payload = new HashMap<String, Object>();
        Maps.putIfNotNull(payload, "measure_time", epoch);
        List<Map<String, Object>> gauges = new LinkedList<Map<String, Object>>();
        List<Map<String, Object>> counters = new LinkedList<Map<String, Object>>();
        for (IMeasure measure : measures) {
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

    private byte[] buildMDPayload(long epoch, List<IMeasure> measures) {
        throw new UnsupportedOperationException("MD payloads not supported yet");
    }
}
