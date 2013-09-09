package com.librato.metrics;

import org.codehaus.jackson.annotate.JsonProperty;

public class Gauge {
    @JsonProperty
    String name;
    @JsonProperty
    Number value;

    public Gauge() {
        // jackson
    }

    public Gauge(String name, Number value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Number getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gauge gauge = (Gauge) o;

        if (name != null ? !name.equals(gauge.name) : gauge.name != null) return false;
        if (value != null ? !value.equals(gauge.value) : gauge.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Gauge{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public static Gauge of(String name, int value) {
        return new Gauge(name, value);
    }
}
