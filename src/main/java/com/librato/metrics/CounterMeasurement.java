package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

import static com.librato.metrics.Preconditions.checkNotNull;

/**
 * Represents a reading from a counter
 */
public class CounterMeasurement implements Measurement {
    private final Number period;
    private final String source;
    private final String name;
    private final Long count;

    public CounterMeasurement(String name, Long count) {
        this(null, null, name, count);
    }

    public CounterMeasurement(String source, String name, Long count) {
        this(source, null, name, count);
    }

    public CounterMeasurement(String source, Number period, String name, Long count) {
        this.source = source;
        this.period = period;
        this.name = checkNotNull(name);
        this.count = checkNotNull(count);
    }

    public String getSource() {
        return source;
    }

    public Number getPeriod() { return period; }

    public String getName() {
        return name;
    }

    public Map<String, Number> toMap() {
        final Map<String, Number> value = new HashMap<String, Number>(1);
        value.put("value", count);
        return value;
    }
}
