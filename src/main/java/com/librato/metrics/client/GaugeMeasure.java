package com.librato.metrics.client;

import java.util.Map;

public class GaugeMeasure extends AbstractMeasure {
    private String source;
    private Double value;
    private Double sum;
    private Double min;
    private Double max;
    private Double sumSquares;
    private Integer count;

    public GaugeMeasure(String name, Double value) {
        super(name);
        this.value = value;
    }

    public GaugeMeasure(String name, Double sum, Integer count, Double min, Double max) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
    }

    public GaugeMeasure(String name, Double sum, Integer count, Double min, Double max, Double sumSquares) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
        this.sumSquares = sumSquares;
    }

    public GaugeMeasure setTime(long epochSeconds) {
        this.epoch = epochSeconds;
        return this;
    }

    public GaugeMeasure setSource(String source) {
        this.source = source;
        return this;
    }

    public GaugeMeasure setMetricAttributes(Map<String, Object> attributes) {
        this.metricAttributes = attributes;
        return this;
    }

    public GaugeMeasure setPeriod(int period) {
        this.period = period;
        return this;
    }
}
