package com.librato.metrics.client;

import java.util.Arrays;
import java.util.Map;

public class Post {
    private final String uri;
    private final Duration connectTimeout;
    private final Duration timeout;
    private final Map<String, String> headers;
    private final byte[] payload;

    public Post(String uri, Duration connectTimeout, Duration timeout, Map<String, String> headers, byte[] payload) {
        this.uri = uri;
        this.connectTimeout = connectTimeout;
        this.timeout = timeout;
        this.headers = headers;
        this.payload = payload;
    }

    public Post(String uri, Duration connectTimeout, Duration timeout, Map<String, String> headers, String payload) {
        this(uri, connectTimeout, timeout, headers, payload.getBytes());
    }

    public Post(String uri, Duration connectTimeout, Duration timeout, Map<String, String> headers, Payload foo) {
        this(uri, connectTimeout, timeout, headers, foo.serialize());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (uri != null ? !uri.equals(post.uri) : post.uri != null)
            return false;
        if (connectTimeout != null ? !connectTimeout.equals(post.connectTimeout) : post.connectTimeout != null)
            return false;
        if (timeout != null ? !timeout.equals(post.timeout) : post.timeout != null)
            return false;
        if (headers != null ? !headers.equals(post.headers) : post.headers != null)
            return false;

        Map payloadMap = Json.deserialize(payload, Map.class);
        Map otherMap = Json.deserialize(post.payload, Map.class);
        return payloadMap.equals(otherMap);
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (connectTimeout != null ? connectTimeout.hashCode() : 0);
        result = 31 * result + (timeout != null ? timeout.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(payload);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Post{");
        sb.append("uri='").append(uri).append('\'');
        sb.append(", connectTimeout=").append(connectTimeout);
        sb.append(", timeout=").append(timeout);
        sb.append(", headers=").append(headers);
        sb.append(", payload=").append(new String(payload));
        sb.append('}');
        return sb.toString();
    }
}
