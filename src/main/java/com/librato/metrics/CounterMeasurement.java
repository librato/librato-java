package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

import static com.librato.metrics.Preconditions.checkNotNull;

/**
 * Represents a reading from a counter
 */
public class CounterMeasurement implements Measurement {
    private final String name;
    private final Long count;

    public CounterMeasurement(String name, Long count) {
        this.name = checkNotNull(name);
        this.count = checkNotNull(count);
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
