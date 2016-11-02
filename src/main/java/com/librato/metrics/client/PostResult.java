package com.librato.metrics.client;

public class PostResult {
    private final byte[] payload;
    private final Exception exception;
    private final HttpResponse response;

    public PostResult(byte[] payload, HttpResponse response) {
        this.payload = payload;
        this.response = response;
        this.exception = null;
    }

    public PostResult(byte[] payload, Exception exception) {
        this.payload = payload;
        this.exception = exception;
        this.response = null;
    }
}
