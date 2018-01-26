package com.librato.metrics.client;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

import javax.xml.bind.DatatypeConverter;

public class AuthorizationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAuthHeaderRejectsEmptyToken() throws Exception {
        Authorization.buildAuthHeader("username", null);
    }

    @Test
    public void testEmptyEmailForAppOpticsToken() {
        final String header = Authorization.buildAuthHeader("", "token");
        Assert.assertEquals("Basic dG9rZW46", header);
    }

    @Test
    public void testProducesTheCorrectHeader() throws Exception {
        final String header = Authorization.buildAuthHeader("username", "token");
        Assert.assertEquals("Basic dXNlcm5hbWU6dG9rZW4=", header);
    }


}
