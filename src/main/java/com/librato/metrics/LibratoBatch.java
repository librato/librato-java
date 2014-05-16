package com.librato.metrics;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
    private static final Logger LOG = LoggerFactory.getLogger(LibratoBatch.class);
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
     * @param postBatchSize size at which to break up the batch
     * @param sanitizer the sanitizer to use for cleaning up metric names to comply with librato api requirements
     * @param timeout time allowed for post
     * @param timeoutUnit unit for timeout
     * @param agentIdentifier a string that identifies the poster (such as the name of a library/program using librato-java)
     * @param httpPoster the {@link com.librato.metrics.HttpPoster} that will send the data to Librato
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
     */
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }

    public void addCounterMeasurement(String name, Long value) {
        addMeasurement(new CounterMeasurement(name, value));
    }

    public void addCounterMeasurement(String source, String name, Long value) {
        addMeasurement(new CounterMeasurement(source, name, value));
    }

    public void addGaugeMeasurement(String name, Number value) {
        addMeasurement(new SingleValueGaugeMeasurement(name, value));
    }

    public void addGaugeMeasurement(String source, String name, Number value) {
        addMeasurement(new SingleValueGaugeMeasurement(source, name, value));
    }

    public void post(String source, long epoch) {
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        payloadMap.put("source", source);
        payloadMap.put("measure_time", epoch);
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

                payloadMap.put(countersKey, counterData);
                payloadMap.put(gaugesKey, gaugeData);
                postPortion(payloadMap);
                payloadMap.remove(gaugesKey);
                payloadMap.remove(countersKey);
                gaugeData.clear();
                counterData.clear();
            }
        }
        LOG.debug("Posted {} measurements", counter);
    }

    private void postPortion(Map<String, Object> chunk) {
        try {
            final String payload = OBJECT_MAPPER.writeValueAsString(chunk);
            final Future<Response> future = httpPoster.post(userAgent, payload);
            final Response response = future.get(timeout, timeoutUnit);
            final int statusCode = response.getStatusCode();
            if (statusCode < 200 || statusCode >= 300) {
                LOG.error("Received an error from Librato API. Code : {}, Message: {}", statusCode, response.getBody());
            }
        } catch (Exception e) {
            LOG.error("Unable to post to Librato API", e);
        }
    }
}
