package com.librato.metrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.librato.metrics.Preconditions.checkNotNull;
import static com.librato.metrics.Preconditions.checkNumeric;

/**
 * A class representing a single gauge reading
 * <p/>
 * See http://dev.librato.com/v1/post/metrics for an explanation of basic vs multi-sample gauge
 */
public class SingleValueGaugeMeasurement implements Measurement {
    private static final Map<String, Object> emptyAttributes = Collections.emptyMap();
    private final Number period;
    private final String source;
    private final String name;
    private final Number reading;
    private final Map<String, Object> metricAttributes;

    public static SingleValueGaugeMeasurementBuilder builder(String name, Number reading) {
        return new SingleValueGaugeMeasurementBuilder(name, reading);
    }

    public SingleValueGaugeMeasurement(String name, Number reading) {
        this(null, null, name, reading);
    }

    public SingleValueGaugeMeasurement(String source, String name, Number reading) {
        this(source, null, name, reading);
    }

    public SingleValueGaugeMeasurement(String source, Number period, String name, Number reading) {
        this(source, period, name, reading, emptyAttributes);
    }

    public SingleValueGaugeMeasurement(String source, Number period, String name, Number reading, Map<String, Object> metricAttributes) {
        try {
            this.source = source;
            this.period = period;
            this.name = checkNotNull(name);
            this.reading = checkNumeric(checkNumeric(reading));
            this.metricAttributes = metricAttributes;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid single-gauge measurement name=" + name, e);
        }
    }

    public Map<String, Object> getMetricAttributes() {
        return metricAttributes;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public Number getPeriod() {
    return period;
  }

    public Map<String, Number> toMap() {
        final Map<String, Number> value = new HashMap<String, Number>(1);
        value.put("value", reading);
        return value;
    }
}
