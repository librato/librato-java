package com.librato.metrics;

import com.librato.metrics.client.Duration;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class LibratoClientAttributes {
    public URI metricURI = URI.create("https://metrics-api.librato.com");
    public URI taggedURI = URI.create("https://measurements-api.librato.com");
    public int batchSize = 500;
    public Duration timeout = new Duration(5, TimeUnit.SECONDS);
    public String email;
    public String token;

}
