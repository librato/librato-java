package com.librato.metrics.client;

public class MDResponseConverter implements IResponseConverter {
    @Override
    public PostResult convert(byte[] payload, HttpResponse response) {
        return new PostResult(true, payload, response);
    }

    @Override
    public PostResult convert(byte[] payload, Exception exception) {
        return new PostResult(true, payload, exception);
    }
}
