package com.librato.metrics.client;

import java.net.URI;

public class LibratoClientBuilder {
    private final LibratoClientAttributes attrs = new LibratoClientAttributes();

    public LibratoClientBuilder(String email, String token) {
        attrs.email = email;
        attrs.token = token;
    }

    public LibratoClientBuilder setMetricURI(String uri) {
        this.attrs.metricURI = URI.create(uri);
        return this;
    }

    public LibratoClientBuilder setTaggedURI(String uri) {
        this.attrs.taggedURI = URI.create(uri);
        return this;
    }

    public LibratoClientBuilder setBatchSize(int batchSize) {
        this.attrs.batchSize = batchSize;
        return this;
    }

    public LibratoClientBuilder setConnectTimeout(Duration timeout) {
        this.attrs.connectTimeout = timeout;
        return this;
    }

    public LibratoClientBuilder setReadTimeout(Duration timeout) {
        this.attrs.readTimeout = timeout;
        return this;
    }

    public LibratoClientBuilder setPoster(IPoster poster) {
        this.attrs.poster = poster;
        return this;
    }

    public LibratoClient build() {
        return new LibratoClient(attrs);
    }
}
