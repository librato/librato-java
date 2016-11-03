package com.librato.metrics.client;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class LibratoClientAttributes {
    public URI uri = URI.create("https://metrics-api.librato.com");
    public int batchSize = 500;
    public Duration connectTimeout = new Duration(5, TimeUnit.SECONDS);
    public Duration readTimeout = new Duration(10, TimeUnit.SECONDS);
    public String email;
    public String token;
    public IPoster poster = new DefaultPoster();
    public int maxInflightRequests = 10;
    public String agentIdentifier = "unknown";

}
