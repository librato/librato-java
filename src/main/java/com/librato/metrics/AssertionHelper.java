package com.librato.metrics;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

final class AssertionHelper {
    private AssertionHelper() {
    }

    static Number numeric(Number number) {
        if (number == null) {
            return null;
        }

        if (isNaN(number.doubleValue()) || isInfinite(number.doubleValue())) {
            throw new IllegalArgumentException(number + " is not a numeric value");
        }

        return number;
    }

    static <T> T notNull(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }

        return object;
    }
}
