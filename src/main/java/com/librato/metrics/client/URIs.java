package com.librato.metrics.client;

import java.net.URI;
import java.net.URISyntaxException;

public class URIs {
    public static URI removePath(URI u) {
        try {
            return new URI(
                    u.getScheme(),
                    u.getUserInfo(), u.getHost(), u.getPort(),
                    null, u.getQuery(),
                    u.getFragment());
        } catch (URISyntaxException e) {
            throw Throwables.propagate(e);
        }
    }
}
