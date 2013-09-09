package com.librato.metrics;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Posts data to an HTTP endpoint.
 */
public interface HttpPoster {

    static final String API_URL = "https://metrics-api.librato.com/v1/metrics";

    /**
     * A generic interface to represent an HTTP response
     */
    public static interface Response {
        int getStatusCode();
        String getBody() throws IOException;
    }

    /**
     * POST the payload as JSON to librato.
     *
     * @param userAgent the user agent to use
     * @param payload the payload to post
     * @return a Future that returns the response
     * @throws IOException on IO-related error
     */
    Future<Response> post(String userAgent, String payload) throws IOException;
}
