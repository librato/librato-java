package com.librato.metrics;

import java.util.regex.Pattern;

/**
 * utils for dealing with the Librato API's requirements
 */
public class APIUtil {
    public static interface Sanitizer {
        public String apply(String name);
    }

    private static final Pattern disallowed = Pattern.compile("([^A-Za-z0-9.:-_]|[\\[\\]]|\\s)");
    private static final int LENGTH_LIMIT = 256;

    /**
     * Metric names restrictions are described <a href="http://dev.librato.com/v1/metrics">here</a>.
     */
    public static final Sanitizer lastPassSanitizer = new Sanitizer() {
        // not sure I understand why brackets need to be specified separately, but behavior is the same in python
        @Override
        public String apply(String name) {
            name = disallowed.matcher(name).replaceAll("");
            if (name.length() > LENGTH_LIMIT) {
                name = name.substring(name.length() - LENGTH_LIMIT, name.length());
            }
            return name;
        }
    };

    /**
     * a stub to be used when the user doesn't specify a sanitizer
     */
    public static final Sanitizer noopSanitizer = new Sanitizer() {
        @Override
        public String apply(String name) {
            return name;
        }
    };
}
