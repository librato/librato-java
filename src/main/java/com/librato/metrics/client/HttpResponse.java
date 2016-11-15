package com.librato.metrics.client;

public interface HttpResponse {
    int getResponseCode();
    byte[] getResponseBody();
}
