package com.librato.metrics.client;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class SDPayload {
    static final ObjectMapper mapper = new ObjectMapper();
    @JsonProperty
    List<Map<String, Object>> gauges = new LinkedList<Map<String, Object>>();

    public SDPayload addGauge(String name, double value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("value", value);
        gauges.add(map);
        return this;
    }

    public byte[] serialize() {
        try {
            return mapper.writeValueAsBytes(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
