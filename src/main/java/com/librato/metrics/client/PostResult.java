package com.librato.metrics.client;

public class PostResult {
    public final boolean md;
    public final byte[] payload;
    public final Exception exception;
    public final HttpResponse response;

    public PostResult(boolean md, byte[] payload, HttpResponse response) {
        this.md = md;
        this.payload = payload;
        this.response = response;
        this.exception = null;
    }

    public PostResult(boolean md, byte[] payload, Exception exception) {
        this.md = md;
        this.payload = payload;
        this.exception = exception;
        this.response = null;
    }

    public boolean isError() {
        if (exception != null) {
            return true;
        } else if (response == null) {
            return true;
        }
        int code = response.getResponseCode();
        if (!md && code == 200) {
            return false;
        }
        if (md && code / 100 == 2) {
            MDResponse response = Json.deserialize(this.response.getResponseBody(), MDResponse.class);
            if (!response.isFailed()) {
                return false;
            }
        }
        return true;
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
