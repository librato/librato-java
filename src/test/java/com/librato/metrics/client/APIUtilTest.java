package com.librato.metrics.client;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@SuppressWarnings("SpellCheckingInspection")
public class APIUtilTest {
    /**
     * Take a string that's a little too long even without the special chars; verify that the extra gets lopped off the front
     * @throws Exception
     */
    @Test
    public void testRemovingIllegalMethods() throws Exception {
        Sanitizer sanitizer = Sanitizer.VALUE_SANITIZER;
        String important = "reallyclutchinfo";
        List<String> illegalCharacters = Arrays.asList("$", "]", "[", "*", "+", "\t");

        StringBuilder testStringBuilder = new StringBuilder();
        testStringBuilder.append("com.less.important.nonunique.prefix.");
        for (int i = 0; i < 32; i++) {
            testStringBuilder.append(important);
            testStringBuilder.append(illegalCharacters.get(i%6));
        }

        String key = testStringBuilder.toString();

        String sanitized = sanitizer.apply(key);

        assertEquals(256, sanitized.length());
        assertEquals("reallyclutchinfo", sanitized.substring(0, 16));
        assertEquals("reallyclutchinfo", sanitized.substring(240, 256));
        for (String illegalCharacter : illegalCharacters) {
            assertFalse("Key still contains illegal character " + illegalCharacter, sanitized.contains(illegalCharacter));
        }
    }
}
