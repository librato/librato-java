package com.librato.metrics;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.librato.metrics.HttpPoster.Response;
import static com.librato.metrics.Preconditions.checkNotNull;

/**
 * A class that represents an aggregation of metric data from a given run
 */
@SuppressWarnings("unused")
public class LibratoBatch {
    public static final int DEFAULT_BATCH_SIZE = 500;
    private static final String LIB_VERSION = Versions.getVersion("META-INF/maven/com.librato.metrics/librato-java/pom.properties", LibratoBatch.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected final List<Measurement> measurements = new ArrayList<Measurement>();
    private final int postBatchSize;
    private final Sanitizer sanitizer;
    private final long timeout;
    private final TimeUnit timeoutUnit;
    private final String userAgent;
    private final HttpPoster httpPoster;

    /**
     * Constructor
     *
     * @param postBatchSize   size at which to break up the batch
     * @param sanitizer       the sanitizer to use for cleaning up metric names to comply with librato api requirements
     * @param timeout         time allowed for post
     * @param timeoutUnit     unit for timeout
     * @param agentIdentifier a string that identifies the poster (such as the name of a library/program using librato-java)
     * @param httpPoster      the {@link com.librato.metrics.HttpPoster} that will send the data to Librato
     */
    public LibratoBatch(int postBatchSize,
                        final Sanitizer sanitizer,
                        long timeout,
                        TimeUnit timeoutUnit,
                        String agentIdentifier,
                        HttpPoster httpPoster) {
        this.postBatchSize = postBatchSize;
        this.sanitizer = new Sanitizer() {
            public String apply(String name) {
                return Sanitizer.LAST_PASS.apply(sanitizer.apply(name));
            }
        };
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
        this.userAgent = String.format("%s librato-java/%s", agentIdentifier, LIB_VERSION);
        this.httpPoster = checkNotNull(httpPoster);
    }

    /**
     * for advanced measurement fu
	 * @param measurement
     */
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }

    public void addCounterMeasurement(String name, Long value) {
        addMeasurement(CounterMeasurement.builder(name, value).build());
    }

    public void addCounterMeasurement(String source, String name, Long value) {
        addMeasurement(CounterMeasurement.builder(name, value)
                .setSource(source)
                .build());
    }

    public void addCounterMeasurement(String source, Number period, String name, Long value) {
        addMeasurement(CounterMeasurement.builder(name, value)
                .setSource(source)
                .setPeriod(period)
                .build());
    }

    public void addGaugeMeasurement(String name, Number value) {
        addMeasurement(SingleValueGaugeMeasurement.builder(name, value).build());
    }

    public void addGaugeMeasurement(String source, String name, Number value) {
        addMeasurement(SingleValueGaugeMeasurement.builder(name, value)
                .setSource(source)
                .build());
    }

    public void addGaugeMeasurement(String source, Number period, String name, Number value) {
        addMeasurement(SingleValueGaugeMeasurement.builder(name, value)
                .setSource(source)
                .setPeriod(period)
                .build());
    }

    public BatchResult post(String source) {
        return post(source, null);
    }

    public BatchResult post(String source, Long epoch) {
        final BatchResult result = new BatchResult();
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        payloadMap.put("source", source);
        if (epoch != null) {
            payloadMap.put("measure_time", epoch);
        }
        final List<Map<String, Object>> gaugeData = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> counterData = new ArrayList<Map<String, Object>>();

        int counter = 0;

        final Iterator<Measurement> measurementIterator = measurements.iterator();
        while (measurementIterator.hasNext()) {
            final Measurement measurement = measurementIterator.next();
            final Map<String, Object> data = new HashMap<String, Object>();
            data.put("name", sanitizer.apply(measurement.getName()));
            if (measurement.getSource() != null) {
                data.put("source", sanitizer.apply(measurement.getSource()));
            }
            if (measurement.getPeriod() != null) {
                data.put("period", measurement.getPeriod());
            }
            if (!measurement.getMetricAttributes().isEmpty()) {
                data.put("attributes", measurement.getMetricAttributes());
            }
            if (measurement.getMeasureTime() != null) {
                data.put("measure_time", measurement.getMeasureTime());
            }
            data.putAll(measurement.toMap());
            if (measurement instanceof CounterMeasurement) {
                counterData.add(data);
            } else {
                gaugeData.add(data);
            }
            counter++;
            if (counter % postBatchSize == 0 || (!measurementIterator.hasNext() && (!counterData.isEmpty() || !gaugeData.isEmpty()))) {
                final String countersKey = "counters";
                final String gaugesKey = "gauges";

                payloadMap.put(countersKey, new ArrayList<Map<String, Object>>(counterData));
                payloadMap.put(gaugesKey, new ArrayList<Map<String, Object>>(gaugeData));
                result.addPostResult(postPortion(payloadMap));
                payloadMap.remove(gaugesKey);
                payloadMap.remove(countersKey);
                gaugeData.clear();
                counterData.clear();
            }
        }
        return result;
    }

    private PostResult postPortion(Map<String, Object> chunk) {
        try {
            final String payload = OBJECT_MAPPER.writeValueAsString(chunk);
            final Future<Response> future = httpPoster.post(userAgent, payload);
            final Response response = getFuture(future);
            final int statusCode = response.getStatusCode();
            String responseBody = response.getBody();
            return new PostResult(chunk, statusCode, responseBody);
        } catch (Exception e) {
            return new PostResult(chunk, e);
        }
    }

    private Response getFuture(Future<Response> future) throws Exception {
        try {
            return future.get(timeout, timeoutUnit);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted");
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
