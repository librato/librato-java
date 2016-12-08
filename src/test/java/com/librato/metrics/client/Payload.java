package com.librato.metrics.client;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
class Payload {
    static final ObjectMapper mapper = new ObjectMapper();
    @JsonProperty
    String source;
    @JsonProperty
    Integer period;
    @JsonProperty("measure_time")
    Long measureTime;
    @JsonProperty
    List<Map<String, Object>> counters = new LinkedList<Map<String, Object>>();
    @JsonProperty
    List<Map<String, Object>> gauges = new LinkedList<Map<String, Object>>();
    @JsonProperty("measurements")
    List<Map<String, Object>> tagged = new LinkedList<Map<String, Object>>();

    public Payload setSource(String source) {
        this.source = source;
        return this;
    }

    public Payload setPeriod(Integer period) {
        this.period = period;
        return this;
    }

    public Payload setMeasureTime(long measureTime) {
        this.measureTime = measureTime;
        return this;
    }

    public Payload addGauge(String name, double sum, long count, double min, double max, double sumSquares) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("sum", sum);
        map.put("count", count);
        map.put("min", min);
        map.put("max", max);
        map.put("sum_squares", sumSquares);
        return addGauge(map);
    }

    public Payload addGauge(String name, double value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("value", value);
        return addGauge(map);
    }

    public Payload addGauge(String name, double value, int period) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("value", value);
        map.put("period", period);
        return addGauge(map);
    }

    public Payload addCounter(String name, long value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("value", value);
        return addCounter(map);
    }

    public Payload addTagged(String name, double sum, Tag... tags) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("sum", sum);
        map.put("count", 1);
        map.put("min", sum);
        map.put("max", sum);
        Map<String, String> tagMap = new HashMap<String, String>();
        map.put("tags", tagMap);
        for (Tag tag : tags) {
            tagMap.put(tag.name, tag.value);
        }
        return addTagged(map);
    }

    private Payload addTagged(Map<String, Object> map) {
        tagged.add(map);
        return this;
    }

    private Payload addCounter(Map<String, Object> map) {
        counters.add(map);
        return this;
    }

    private Payload addGauge(Map<String, Object> map) {
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
