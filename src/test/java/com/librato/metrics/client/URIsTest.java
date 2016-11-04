package com.librato.metrics.client;

import org.junit.Test;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class URIsTest {

    @Test
    public void testURIPath() throws Exception {
        assertThat(URIs.removePath(URI.create("https://metrics-api.librato.com/")),
                equalTo(URI.create("https://metrics-api.librato.com")));
        assertThat(URIs.removePath(URI.create("https://metrics-api.librato.com")),
                equalTo(URI.create("https://metrics-api.librato.com")));
        assertThat(URIs.removePath(URI.create("https://metrics-api.librato.com:443")),
                equalTo(URI.create("https://metrics-api.librato.com:443")));
        assertThat(URIs.removePath(URI.create("https://metrics-api.librato.com/v1/metrics")),
                equalTo(URI.create("https://metrics-api.librato.com")));

    }
}
