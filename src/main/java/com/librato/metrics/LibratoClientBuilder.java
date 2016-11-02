package com.librato.metrics;

import com.librato.metrics.client.Duration;

import java.net.URI;

public class LibratoClientBuilder {
    private final LibratoClientAttributes attrs = new LibratoClientAttributes();

    public LibratoClientBuilder(String email, String token) {
        attrs.email = email;
        attrs.token = token;
    }

    public LibratoClientBuilder setMetricURI(URI uri) {
        this.attrs.metricURI = uri;
        return this;
    }

    public LibratoClientBuilder setTaggedURI(URI uri) {
        this.attrs.taggedURI = uri;
        return this;
    }

    public LibratoClientBuilder setBatchSize(int batchSize) {
        this.attrs.batchSize = batchSize;
        return this;
    }

    public LibratoClientBuilder setTimeout(Duration timeout) {
        this.attrs.timeout = timeout;
        return this;
    }
}
