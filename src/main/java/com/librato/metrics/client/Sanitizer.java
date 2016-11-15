package com.librato.metrics.client;

import java.util.regex.Pattern;

/**
 * Filters out unwanted characters
 */
public interface Sanitizer {

    /**
     * Performs an identity transform on the input
     */
    @SuppressWarnings("unused")
    public static final Sanitizer NO_OP = new Sanitizer() {
        public String apply(String name) {
            return name;
        }
    };

    /**
     * Metric names restrictions are described <a href="http://dev.librato.com/v1/metrics">here</a>.
     */
    @SuppressWarnings("unused")
    public static final Sanitizer LAST_PASS = new Sanitizer() {
        private final Pattern disallowedCharacters = Pattern.compile("([^A-Za-z0-9.:\\-_]|[\\[\\]]|\\s)");
        private final int lengthLimit = 256;

        public String apply(String name) {
            if (name == null) {
                return null;
            }
            final String sanitized = disallowedCharacters.matcher(name).replaceAll("");
            if (sanitized.length() > lengthLimit) {
                return sanitized.substring(sanitized.length() - lengthLimit, sanitized.length());
            }
            return sanitized;
        }
    };

    /**
     * Apply the sanitizer to the input
     *
     * @param name the input
     * @return the sanitized output
     */
    public String apply(String name);
}
