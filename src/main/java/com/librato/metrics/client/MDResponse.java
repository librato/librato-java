package com.librato.metrics.client;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.List;

public class MDResponse {
    @JsonProperty("errors")
    List<Object> errors = Collections.emptyList();

    public boolean isFailed() {
        return !errors.isEmpty();
    }
}
