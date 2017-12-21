package com.librato.metrics.client;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SanitizerTest extends TestCase {

    public void testNamesRemove() {
        final List<String> removeThese = new ArrayList<String>() {{
            this.add("*");
            this.add("?");
            this.add("&");
            this.add("+");
            this.add("/");
            this.add(" ");
            this.add("");
        }};

        for (final String remove : removeThese) {
            final String testString = "one" + remove + "two";
            final String sanitized = Sanitizer.NAME_SANITIZER.apply(testString);
            assertThat(sanitized, equalTo("onetwo"));
        }
    }

    public void testSanitizeValues() {
        final List<String> removeThese = new ArrayList<String>() {{
            this.add("*");
            this.add("&");
            this.add("+");
            this.add("$");
            this.add("[");
            this.add("]");
            this.add("\t");
            this.add(" ");
            this.add("");
        }};

        for (final String remove : removeThese) {
            final String testString = "one" + remove + "two";
            final String sanitized = Sanitizer.VALUE_SANITIZER.apply(testString);
            assertThat(sanitized, equalTo("onetwo"));
        }
    }

    public void testKeepValueSlashes() {
        // had a specific problem involving these...
        final String testString = "one/two";
        final String sanitized = Sanitizer.VALUE_SANITIZER.apply(testString);
        assertThat(sanitized, equalTo(testString));
    }
}
