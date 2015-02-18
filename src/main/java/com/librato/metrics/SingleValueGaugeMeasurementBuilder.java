package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

public class SingleValueGaugeMeasurementBuilder {
    private final String name;
    private final Number reading;
    private String source = null;
    private Number period = null;
    private Map<String, Object> metricAttributes = new HashMap<String, Object>();
    private Long measureTime;

    public SingleValueGaugeMeasurementBuilder(String name, Number reading) {
        this.name = name;
        this.reading = reading;
    }

    public SingleValueGaugeMeasurementBuilder setSource(String source) {
        this.source = source;
        return this;
    }

    public SingleValueGaugeMeasurementBuilder setPeriod(Number period) {
        this.period = period;
        return this;
    }

    public SingleValueGaugeMeasurementBuilder setMetricAttribute(String name, Object value) {
        metricAttributes.put(name, value);
        return this;
    }

    public SingleValueGaugeMeasurementBuilder setMeasureTime(Long measureTime) {
        this.measureTime = measureTime;
        return this;
    }

    public SingleValueGaugeMeasurement build() {
        return new SingleValueGaugeMeasurement(source, period, name, reading, metricAttributes, measureTime);
    }
}
