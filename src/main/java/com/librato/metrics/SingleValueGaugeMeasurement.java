package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

import static com.librato.metrics.AssertionHelper.notNull;
import static com.librato.metrics.AssertionHelper.numeric;

/**
 * User: mihasya
 * Date: 6/17/12
 * Time: 10:01 PM
 * A class representing a single gauge reading
 * <p/>
 * See http://dev.librato.com/v1/post/metrics for an explanation of basic vs multi-sample gauge
 */
public class SingleValueGaugeMeasurement implements Measurement {

    private final String name;
    private final Number reading;

    public SingleValueGaugeMeasurement(String name, Number reading) {
        this.name = notNull(name);
        this.reading = numeric(numeric(reading));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Number> toMap() {
        Map<String, Number> value = new HashMap<String, Number>();
        value.put("value", reading);
        return value;
    }
}
