package com.librato.metrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.librato.metrics.Preconditions.checkNotNull;

/**
 * Represents a reading from a counter
 */
public class CounterMeasurement implements Measurement {
    private static final Map<String, Object> emptyAttributes = Collections.emptyMap();
    private final Number period;
    private final String source;
    private final String name;
    private final Long count;
    private final Map<String, Object> metricAttributes;
    private final Long measureTime;

    public static CounterMeasurementBuilder builder(String name, Long count) {
        return new CounterMeasurementBuilder(name, count);
    }

    public CounterMeasurement(String name, Long count) {
        this(null, null, name, count);
    }

    public CounterMeasurement(String source, String name, Long count) {
        this(source, null, name, count);
    }

    public CounterMeasurement(String source, Number period, String name, Long count) {
        this(source, period, name, count, emptyAttributes, null);
    }

    public CounterMeasurement(String source, Number period, String name, Long count, Map<String, Object> metricAttributes, Long measureTime) {
        this.source = source;
        this.period = period;
        this.name = checkNotNull(name);
        this.count = checkNotNull(count);
        this.metricAttributes = metricAttributes;
        this.measureTime = measureTime;
    }

    public Long getMeasureTime() {
        return measureTime;
    }

    public Map<String, Object> getMetricAttributes() {
        return metricAttributes;
    }

    public String getSource() {
        return source;
    }

    public Number getPeriod() {
        return period;
    }

    public String getName() {
        return name;
    }

    public Map<String, Number> toMap() {
        final Map<String, Number> value = new HashMap<String, Number>(1);
        value.put("value", count);
        return value;
    }
}
