package com.librato.metrics.client;

public class Throwables {
    public static RuntimeException propagate(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw new RuntimeException(throwable);
    }
}
