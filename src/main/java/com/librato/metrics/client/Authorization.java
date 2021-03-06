package com.librato.metrics.client;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;

public class Authorization {
    private Authorization() {
        // utility class, do not construct
    }

    /**
     * Builds a new HTTP Authorization header for Librato API requests
     *
     * @param username the Librato username
     * @param token    the Librato token
     * @return the Authorization header value
     */
    public static String buildAuthHeader(String username, String token) {
        if (username == null || "".equals(username)) {
            throw new IllegalArgumentException("Username must be specified");
        }
        if (token == null || "".equals(token)) {
            throw new IllegalArgumentException("Token must be specified");
        }
        return String.format("Basic %s", base64Encode((username + ":" + token).getBytes(Charset.forName("UTF-8"))));
    }

    private static String base64Encode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }
}
