package com.librato.metrics;

import com.librato.metrics.client.DefaultPoster;
import com.librato.metrics.client.Duration;
import com.librato.metrics.client.IPoster;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class LibratoClientAttributes {
    public URI metricURI = URI.create("https://metrics-api.librato.com");
    public URI taggedURI = URI.create("https://measurements-api.librato.com");
    public int batchSize = 500;
    public Duration connectTimeout = new Duration(5, TimeUnit.SECONDS);
    public Duration readTimeout = new Duration(5, TimeUnit.SECONDS);
    public String email;
    public String token;
    public IPoster poster = new DefaultPoster();

}
