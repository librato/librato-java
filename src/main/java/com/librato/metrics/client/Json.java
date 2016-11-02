package com.librato.metrics.client;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> byte[] serialize(T data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
