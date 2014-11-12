package com.librato.metrics;

import java.util.Map;

public class MultiSampleGaugeMeasurementBuilder {
    private final String name;
    private String source;
    private Number period;
    private Long count;
    private Number sum;
    private Number max;
    private Number min;
    private Number sumSquares;
    private Map<String, Object> metricAttributes;

    public MultiSampleGaugeMeasurementBuilder(String name) {
        this.name = name;
    }

    public MultiSampleGaugeMeasurementBuilder setSource(String source) {
        this.source = source;
        return this;
    }

    public MultiSampleGaugeMeasurementBuilder setPeriod(Number period) {
        this.period = period;
        return this;
    }

    public MultiSampleGaugeMeasurementBuilder setCount(Long count) {
        this.count = count;
        return this;
    }

    public MultiSampleGaugeMeasurementBuilder setSum(Number sum) {
        this.sum = sum;
        return this;
    }

    public MultiSampleGaugeMeasurementBuilder setMax(Number max) {
        this.max = max;
        return this;
    }

    public MultiSampleGaugeMeasurementBuilder setMin(Number min) {
        this.min = min;
        return this;
    }

    public MultiSampleGaugeMeasurementBuilder setSumSquares(Number sumSquares) {
        this.sumSquares = sumSquares;
        return this;
    }

    public MultiSampleGaugeMeasurementBuilder setMetricAttribute(String name, Object value) {
        metricAttributes.put(name, value);
        return this;
    }

    public MultiSampleGaugeMeasurement build() {
        return new MultiSampleGaugeMeasurement(source, period, name, count, sum, max, min, sumSquares, metricAttributes);
    }
}
