package com.librato.metrics.client;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

/**
 * For users which need control of actually making an HTTP submission
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = DefaultPoster.class)
public interface IPoster {
    HttpResponse post(String uri, Duration connectTimeout, Duration readTimeout, Map<String, String> headers, byte[] payload);
}
