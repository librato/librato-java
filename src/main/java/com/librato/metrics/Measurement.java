package com.librato.metrics;

import java.util.Map;

/**
 * Represents a Librato measurement
 */
public interface Measurement {
    public String getName();
    public Map<String, Number> toMap();
}
