package com.librato.metrics.client;

import java.util.Map;

/**
 * Represents a client
 */
interface IMeasure {

    boolean isTagged();

    boolean isGauge();

    Map<String,Object> toMap();
}
