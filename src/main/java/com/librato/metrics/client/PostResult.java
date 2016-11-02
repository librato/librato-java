package com.librato.metrics.client;

public class PostResult {
    public final byte[] payload;
    public final Exception exception;
    public final HttpResponse response;

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

    public boolean isError() {
        return exception != null || (response != null && response.getResponseCode() / 100 != 2);
    }

    @Override
    public String toString() {
        if (exception != null) {
            return exception.toString();
        }
        if (response != null) {
            int code = response.getResponseCode();
            byte[] body = response.getResponseBody();
            return "code:" + code + " response:" + new String(body);
        }
        return "invalid post result";
    }
}
