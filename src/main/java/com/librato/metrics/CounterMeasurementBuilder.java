package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

public class CounterMeasurementBuilder {
    private final String name;
    private final Long count;
    private String source = null;
    private Number period = null;
    private Map<String, Object> metricAttributes = new HashMap<String, Object>();

    public CounterMeasurementBuilder(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    public CounterMeasurementBuilder setSource(String source) {
        this.source = source;
        return this;
    }

    public CounterMeasurementBuilder setPeriod(Number period) {
        this.period = period;
        return this;
    }

    public CounterMeasurementBuilder setMetricAttribute(String name, Object value) {
        metricAttributes.put(name, value);
        return this;
    }

    public CounterMeasurement build() {
        return new CounterMeasurement(source, period, name, count, metricAttributes);
    }
}
