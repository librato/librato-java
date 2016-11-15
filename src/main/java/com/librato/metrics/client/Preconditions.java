package com.librato.metrics.client;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * Helpful static methods, kinda like Guava.
 */
final class Preconditions {
    private Preconditions() {
        // helper class, do not instantiate
    }

    static Number checkNumeric(Number number) {
        if (number == null) {
            return null;
        }
        final double doubleValue = number.doubleValue();
        if (isNaN(doubleValue) || isInfinite(doubleValue)) {
            throw new IllegalArgumentException(number + " is not a numeric value");
        }
        return number;
    }

    static <T> T checkNotNull(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        return object;
    }
}
