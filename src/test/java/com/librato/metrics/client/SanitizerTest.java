package com.librato.metrics.client;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
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
            final String sanitized = Sanitizer.METRIC_NAME_SANITIZER.apply(testString);
            assertThat(sanitized, equalTo("onetwo"));
        }
    }

    public void testKeepValueSlashes() {
        // had a specific problem involving these...
        final String testString = "one/two";
        final String sanitized = Sanitizer.TAG_VALUE_SANITIZER.apply(testString);
        assertThat(sanitized, equalTo(testString));
    }

    /**
     * Metric names: take a string that's a little too long even without the special chars; verify
     * that the extra gets lopped off the front
     */
    public void testRemovingIllegalMethods() {
        Sanitizer sanitizer = Sanitizer.METRIC_NAME_SANITIZER;
        String important = "abcdefghijklmno";
        List<String> illegalCharacters = Arrays.asList("$", "]", "[", "*", "+", "\t");

        StringBuilder testStringBuilder = new StringBuilder();
        testStringBuilder.append("com.less.important.nonunique.prefix.");
        for (int i = 0; i < 32; i++) {
            testStringBuilder.append(important);
            testStringBuilder.append(illegalCharacters.get(i % illegalCharacters.size()));
        }

        String key = testStringBuilder.toString();

        String sanitized = sanitizer.apply(key);

        assertEquals(255, sanitized.length());
        assertEquals(important, sanitized.substring(0, 15));
        assertEquals(important, sanitized.substring(240, 255));
        for (String illegalCharacter : illegalCharacters) {
            assertFalse("Key still contains illegal character " + illegalCharacter, sanitized.contains(illegalCharacter));
        }
    }
}
