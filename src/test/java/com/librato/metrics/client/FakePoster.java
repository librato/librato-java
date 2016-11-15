package com.librato.metrics.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FakePoster implements IPoster {
    int responseCode = 200;
    String response = "";
    List<Post> posts = new LinkedList<Post>();

    @Override
    public HttpResponse post(String uri,
                             Duration connectTimeout,
                             Duration readTimeout,
                             Map<String, String> headers,
                             byte[] payload) {
        posts.add(new Post(uri, connectTimeout, readTimeout, headers, payload));
        return new HttpResponse() {
            @Override
            public int getResponseCode() {
                return responseCode;
            }

            @Override
            public byte[] getResponseBody() {
                return response.getBytes();
            }
        };
    }

    public FakePoster setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public FakePoster setResponse(String response) {
        this.response = response;
        return this;
    }
}
