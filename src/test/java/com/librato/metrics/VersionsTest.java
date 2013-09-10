package com.librato.metrics;

import org.junit.Assert;
import org.junit.Test;

public class VersionsTest {

    @Test
    public void testFindsTheVersion() throws Exception {
        final String version = Versions.getVersion("com/librato/metrics/valid.pom.properties", Versions.class);
        Assert.assertEquals("0.0.10", version);
    }

    @Test
    public void testDoesNotFindThePath() throws Exception {
        final String version = Versions.getVersion("com/librato/metrics/does-not-exist", Versions.class);
        Assert.assertEquals("unknown", version);
    }

    @Test
    public void testDoesNotFindTheVersion() throws Exception {
        final String version = Versions.getVersion("com/librato/metrics/invalid.pom.properties", Versions.class);
        Assert.assertEquals("unknown", version);
    }

}
