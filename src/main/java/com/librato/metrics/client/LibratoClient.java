package com.librato.metrics.client;

import com.librato.metrics.LibratoClientAttributes;
import com.librato.metrics.LibratoClientBuilder;

import java.net.URI;

/**
 * The main class that should be used to access the Librato API
 */
public class LibratoClient {
    private final URI metricURI;
    private final URI taggedURI;
    private final int batchSize;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final String email;
    private final String token;
    private final IPoster poster;

    public static LibratoClientBuilder builder(String email, String token) {
        return new LibratoClientBuilder(email, token);
    }

    private LibratoClient(LibratoClientAttributes attrs) {
        this.metricURI = attrs.metricURI;
        this.taggedURI = attrs.taggedURI;
        this.batchSize = attrs.batchSize;
        this.connectTimeout = attrs.connectTimeout;
        this.readTimeout = attrs.readTimeout;
        this.email = attrs.email;
        this.token = attrs.token;
        this.poster = attrs.poster;
    }

    public void postMeasures(Measures measures) {

    }
}
