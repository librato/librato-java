package com.librato.metrics.client;

import java.util.Map;

public class GaugeMeasure extends AbstractMeasure {
    private String source;
    private Double value;
    private Double sum;
    private Double min;
    private Double max;
    private Double sumSquares;
    private Long count;

    public GaugeMeasure(String name, double value) {
        super(name);
        this.value = value;
    }

    public GaugeMeasure(String name, double sum, long count, double min, double max) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
    }

    public GaugeMeasure(String name, double sum, long count, double min, double max, double sumSquares) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
        this.sumSquares = sumSquares;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        Maps.putIfNotNull(map, "measure_time", epoch);
        Maps.putIfNotNull(map, "source", source);
        Maps.putIfNotNull(map, "value", value);
        Maps.putIfNotNull(map, "sum", sum);
        Maps.putIfNotNull(map, "count", count);
        Maps.putIfNotNull(map, "min", min);
        Maps.putIfNotNull(map, "max", max);
        Maps.putIfNotNull(map, "sum_squares", sumSquares);
        return map;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public boolean isGauge() {
        return true;
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
