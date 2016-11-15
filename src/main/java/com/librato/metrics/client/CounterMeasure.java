package com.librato.metrics.client;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterMeasure that = (CounterMeasure) o;

        if (value != that.value) return false;
        return source != null ? source.equals(that.source) : that.source == null;

    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("source='").append(source).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
