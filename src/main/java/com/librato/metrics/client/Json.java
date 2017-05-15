package com.librato.metrics.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,  false);
    }

    public static <T> byte[] serialize(T data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> klass) {
        try {
            return mapper.readValue(data, klass);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
