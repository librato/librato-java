package com.librato.metrics.client;

import java.util.Map;

/**
 * Represents a client
 */
public interface IMeasure {

    boolean isTagged();

    boolean isGauge();

    Map<String,Object> toMap();
}
