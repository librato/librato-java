package com.librato.metrics.client;

import java.util.regex.Pattern;

/**
 * Filters out unwanted characters and truncates
 */
public abstract class Sanitizer {

    private static String sanitize(final String unclean, final Pattern disallowedChars, final int maxLength) {
        if (unclean == null) {
            return null;
        }
        final String sanitized = disallowedChars.matcher(unclean).replaceAll("");
        if (sanitized.length() > maxLength) {
            return sanitized.substring(sanitized.length() - maxLength, sanitized.length());
        }
        return sanitized;
    }

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
    public static final Sanitizer METRIC_NAME_SANITIZER = new Sanitizer() {
        private final int lengthLimit = 255;
        // "replace anything that isn't a word char, dash, dot, colon or underscore"
        private final Pattern disallowedCharacters = Pattern.compile("[^-.:_\\w]");

        @Override
        public String apply(final String name) {
            return sanitize(name, disallowedCharacters, lengthLimit);
        }
    };

    @SuppressWarnings("unused")
    public static final Sanitizer TAG_NAME_SANITIZER = new Sanitizer() {
        private final int lengthLimit = 64;
        // "replace anything that isn't a word char, dash, dot, colon or underscore"
        private final Pattern disallowedCharacters = Pattern.compile("[^-.:_\\w]");

        @Override
        public String apply(final String name) {
            return sanitize(name, disallowedCharacters, lengthLimit);
        }
    };

    @SuppressWarnings("unused")
    public static final Sanitizer TAG_VALUE_SANITIZER = new Sanitizer() {
        private final int lengthLimit = 255;
        // "replace anything that isn't a word char, dash, dot, colon, underscore, question mark, slash or space"
        private final Pattern disallowedCharacters = Pattern.compile("[^-.:_?\\\\/\\w ]");

        @Override
        public String apply(final String value) {
            return sanitize(value, disallowedCharacters, lengthLimit);
        }
    };

    @SuppressWarnings("unused")
    public static final Sanitizer SOURCE_SANITIZER = new Sanitizer() {
        private final int lengthLimit = 255;
        // "replace anything that isn't a letter, number, dash, dot, colon or underscore"
        private final Pattern disallowedCharacters = Pattern.compile("[^-:A-Za-z0-9_.]");

        @Override
        public String apply(final String source) {
            return sanitize(source, disallowedCharacters, lengthLimit);
        }
    };

    /**
     * Apply the sanitizer to the input
     *
     * @param input the input
     * @return the sanitized output
     */
    public abstract String apply(String input);
}
