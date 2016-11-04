package com.librato.metrics.client;

public class Tag {
    public final String name;
    public final String value;

    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
