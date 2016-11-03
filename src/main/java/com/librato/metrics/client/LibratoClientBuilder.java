package com.librato.metrics.client;

import java.net.URI;

public class LibratoClientBuilder {
    private final LibratoClientAttributes attrs = new LibratoClientAttributes();

    public LibratoClientBuilder(String email, String token) {
        attrs.email = email;
        attrs.token = token;
    }

    public LibratoClientBuilder setURI(String uri) {
        this.attrs.uri = URI.create(uri);
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

    public LibratoClientBuilder setAgentIdentifier(String identifier) {
        this.attrs.agentIdentifier = identifier;
        return this;
    }

    public LibratoClient build() {
        return new LibratoClient(attrs);
    }
}
