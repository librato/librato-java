package com.librato.metrics;

import java.util.Map;

/**
 * Represents a Librato measurement
 */
public interface Measurement {

    /**
     * @return the source of the measurement
     */
    public String getSource();

    /**
     * @return the name of the measurement
     */
    public String getName();

    /**
     * @return the period of the measurement
     */
    public Number getPeriod();

    /**
     * @return a map of metric names to numbers
     */
    public Map<String, Number> toMap();
}
