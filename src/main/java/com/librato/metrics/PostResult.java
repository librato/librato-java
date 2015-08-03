package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the status of a batch
 */
public class PostResult {
    private final Integer statusCode;
    private final Exception exception;
    private final Map<String,Object> data = new HashMap<String, Object>();
    private final String response;

    public PostResult(Map<String, Object> chunk, int statusCode, String response) {
        this.data.putAll(chunk);
        this.statusCode = statusCode;
        this.exception = null;
        this.response = response;
    }

    public PostResult(Map<String, Object> chunk, Exception e) {
        this.data.putAll(chunk);
        this.exception = e;
        this.statusCode = null;
        this.response = null;
    }

    public boolean success() {
        return statusCode != null && statusCode / 100 == 2;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public Exception getException() {
        return exception;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "statusCode=" + statusCode +
                ", exception=" + exception +
                ", data=" + data +
                ", response='" + response + '\'' +
                '}';
    }
}
