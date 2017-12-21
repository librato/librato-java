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
     * Metric name restrictions are described <a href="http://dev.librato.com/v1/metrics">here</a>.
     */
    @SuppressWarnings("unused")
    public static final Sanitizer NAME_SANITIZER = new Sanitizer() {
        private final int lengthLimit = 64;
        // "disallow anything that isn't a word char, dash, dot, colon or underscore"
        private final Pattern disallowedCharacters = Pattern.compile("[^-.:_\\w]");

        @Override
        public String apply(final String name) {
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

    @SuppressWarnings("unused")
    public static final Sanitizer VALUE_SANITIZER = new Sanitizer() {
        private final int lengthLimit = 256;
        // "disallow anything that isn't a word char, dash, dot, colon, underscore, question mark or slash"
        private final Pattern disallowedCharacters = Pattern.compile("[^-.:_?\\\\/\\w ]");

        public String apply(final String value) {
            if (value == null) {
                return null;
            }
            final String sanitized = disallowedCharacters.matcher(value).replaceAll("");
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
