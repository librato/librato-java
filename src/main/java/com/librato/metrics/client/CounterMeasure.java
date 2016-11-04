package com.librato.metrics.client;

import com.librato.metrics.Sanitizer;

import java.util.Map;

public class CounterMeasure extends AbstractMeasure {
    private String source;
    private final long value;

    public CounterMeasure(String name, long value) {
        super(name);
        this.value = value;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        Maps.putIfNotNull(map, "measure_time", epoch);
        Maps.putIfNotNull(map, "source", Sanitizer.LAST_PASS.apply(source));
        Maps.putIfNotNull(map, "value", value);
        return map;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public boolean isGauge() {
        return false;
    }

    public CounterMeasure setSource(String source) {
        this.source = source;
        return this;
    }

    public CounterMeasure setMetricAttributes(Map<String, Object> attributes) {
        this.metricAttributes = attributes;
        return this;
    }

}
