package com.librato.metrics;

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
    private final String name;
    private final Number reading;

    public SingleValueGaugeMeasurement(String name, Number reading) {
        try {
            this.name = checkNotNull(name);
            this.reading = checkNumeric(checkNumeric(reading));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid single-gauge measurement name=" + name, e);
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, Number> toMap() {
        final Map<String, Number> value = new HashMap<String, Number>();
        value.put("value", reading);
        return value;
    }
}
