package com.librato.metrics;

import org.junit.Assert;
import org.junit.Test;

public class SanitizerTest {

    @Test
    public void testLastPassSanitizer() throws Exception {
        Assert.assertEquals("foo-bar", Sanitizer.LAST_PASS.apply("foo-bar"));
        Assert.assertEquals("foo_bar", Sanitizer.LAST_PASS.apply("foo_bar"));
    }
}
