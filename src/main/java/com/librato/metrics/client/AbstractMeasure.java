package com.librato.metrics.client;

import java.util.Collections;
import java.util.Map;

abstract class AbstractMeasure implements IMeasure {
    final String name;
    Map<String, Object> metricAttributes = Collections.emptyMap();
    Integer period;
    Long epoch;

    public AbstractMeasure(String name) {
        this.name = name;
    }
}

