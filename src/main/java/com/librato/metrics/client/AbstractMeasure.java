package com.librato.metrics.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractMeasure implements IMeasure {
    final String name;
    Map<String, Object> metricAttributes = Collections.emptyMap();
    Integer period;
    Long epoch;

    public AbstractMeasure(AbstractMeasure measure) {
        this.name = measure.name;
        this.metricAttributes = measure.metricAttributes;
        this.period = measure.period;
        this.epoch = measure.epoch;
    }

    public AbstractMeasure(String name) {
        this.name = name;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", Sanitizer.LAST_PASS.apply(name));
        Maps.putIfNotNull(map, "period", period);
        Maps.putIfNotEmpty(map, "attributes", metricAttributes);
        return map;
    }

    public String getName() {
        return name;
    }
}

