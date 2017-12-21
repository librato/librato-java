package com.librato.metrics.client;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@SuppressWarnings("SpellCheckingInspection")
public class APIUtilTest {
    /**
     * Take a string that's a little too long even without the special chars; verify that the extra gets lopped off the front
     */
    @Test
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
