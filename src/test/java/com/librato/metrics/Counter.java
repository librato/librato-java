package com.librato.metrics;

import org.codehaus.jackson.annotate.JsonProperty;

public class Counter {
    @JsonProperty
    String source;
    @JsonProperty
    String name;
    @JsonProperty
    Number value;

    public Counter() {
        // jackson
    }

    public Counter(String name, Number value) {
        this(null, name, value);
    }

    public Counter(String source, String name, Number value) {
        this.source = source;
        this.name = name;
        this.value = value;
    }

    public static Counter of(String source, String name, Number value) {
        return new Counter(source, name, value);
    }

    public static Counter of(String name, Number value) {
        return new Counter(name, value);
    }

    String getSource() {
        return source;
    }

    String getName() {
        return name;
    }

    Number getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Counter counter = (Counter) o;

        if (source != null ? !source.equals(counter.source) : counter.source != null) return false;
        if (name != null ? !name.equals(counter.name) : counter.name != null) return false;
        if (value != null ? !value.equals(counter.value) : counter.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "source='" + source + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
