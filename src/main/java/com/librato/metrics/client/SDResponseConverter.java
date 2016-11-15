package com.librato.metrics.client;

public class SDResponseConverter implements IResponseConverter {
    @Override
    public PostResult convert(byte[] payload, HttpResponse response) {
        return new PostResult(false, payload, response);
    }

    @Override
    public PostResult convert(byte[] payload, Exception exception) {
        return new PostResult(false, payload, exception);
    }
}
