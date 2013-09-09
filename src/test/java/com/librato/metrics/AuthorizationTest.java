package com.librato.metrics;

import org.junit.Assert;
import org.junit.Test;

public class AuthorizationTest {
    @Test(expected = IllegalArgumentException.class)
    public void testAuthHeaderRejectsEmptyUsername() throws Exception {
        Authorization.buildAuthHeader(null, "token");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAuthHeaderRejectsEmptyToken() throws Exception {
        Authorization.buildAuthHeader("username", null);
    }

    @Test
    public void testProducesTheCorrectHeader() throws Exception {
        final String header = Authorization.buildAuthHeader("username", "token");
        Assert.assertEquals("Basic dXNlcm5hbWU6dG9rZW4=", header);
    }
}
