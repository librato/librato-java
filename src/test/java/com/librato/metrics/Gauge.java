package com.librato.metrics;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.Map;

public class Gauge {
    @JsonProperty
    String source;
    @JsonProperty
    Number period;
    @JsonProperty
    String name;
    @JsonProperty
    Number value;
    @JsonProperty
    Map<String, Object> attributes = Collections.emptyMap();

    public Gauge() {
        // jackson
    }

    public Gauge(String name, Number value) {
        this(null, null, name, value);
    }

    public Gauge(String source, String name, Number value) {
        this(source, null, name, value);
    }

    public Gauge(String source, Number period, String name, Number value) {
        this.source = source;
        this.period = period;
        this.name = name;
        this.value = value;
    }

    public static Gauge of(String name, int value) {
        return new Gauge(name, value);
    }

    public static Gauge of(String source, String name, int value) {
        return new Gauge(source, name, value);
    }

    public static Gauge of(String source, Number period, String name, int value) {
        return new Gauge(source, period, name, value);
    }

    public String getSource() {
        return source;
    }

    public Number getPeriod() {
        return period;
    }

    public String getName() {
        return name;
    }

    public Number getValue() {
        return value;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gauge gauge = (Gauge) o;

        if (source != null ? !source.equals(gauge.source) : gauge.source != null) return false;
        if (period != null ? !period.equals(gauge.period) : gauge.period != null) return false;
        if (name != null ? !name.equals(gauge.name) : gauge.name != null) return false;
        if (value != null ? !value.equals(gauge.value) : gauge.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Gauge{" +
                "source='" + source + '\'' +
                ", period='" + period + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
