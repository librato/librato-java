package com.librato.metrics;

import java.io.IOException;

public class FakeResponse implements HttpPoster.Response {
    private final int statusCode;
    private final String body;

    public FakeResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public FakeResponse(int statusCode) {
        this(statusCode, "");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() throws IOException {
        return body;
    }
}
