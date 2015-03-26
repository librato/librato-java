package com.librato.metrics;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Payload {
    public static Payload parse(String value) {
        try {
            return new ObjectMapper().readValue(value, Payload.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @JsonProperty
    String source;
    @JsonProperty
    List<Counter> counters;
    @JsonProperty
    List<Gauge> gauges;
    @JsonProperty("measure_time")
    Long measureTime;

    String getSource() {
        return source;
    }

    List<Counter> getCounters() {
        return counters;
    }

    public Long getMeasureTime() {
        return measureTime;
    }

    public List<Gauge> getGauges() {
        return gauges;
    }
}
