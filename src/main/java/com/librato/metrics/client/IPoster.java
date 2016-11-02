package com.librato.metrics.client;

import java.util.Map;

/**
 * For users which need control of actually making an HTTP submission
 */
public interface IPoster {
    HttpResponse post(String uri, Duration connectTimeout, Duration readTimeout, Map<String, String> headers, byte[] payload);
}
