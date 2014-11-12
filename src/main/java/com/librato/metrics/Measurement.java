package com.librato.metrics;

import java.util.Map;

/**
 * Represents a Librato measurement
 */
public interface Measurement {

    /**
     * @return the source of the measurement
     */
    String getSource();

    /**
     * @return the name of the measurement
     */
    String getName();

    /**
     * @return the period of the measurement
     */
    Number getPeriod();

    /**
     * @return a map of metric names to numbers
     */
    Map<String, Number> toMap();

    /**
     * @return the metric attributes that should be applied to this metric when created
     */
    Map<String, Object> getMetricAttributes();
}
