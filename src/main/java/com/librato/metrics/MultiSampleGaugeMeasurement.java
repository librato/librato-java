package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

import static com.librato.metrics.AssertionHelper.notNull;
import static com.librato.metrics.AssertionHelper.numeric;


/**
 * User: mihasya
 * Date: 6/17/12
 * Time: 10:06 PM
 * a class for representing a gauge reading that might come from multiple samples
 * <p/>
 * See http://dev.librato.com/v1/post/metrics for why some fields are optional
 */
public class MultiSampleGaugeMeasurement implements Measurement {
    private final String name;
    private final Long count;
    private final Number sum;
    private final Number max;
    private final Number min;
    private final Number sum_squares;

    public MultiSampleGaugeMeasurement(String name, Long count, Number sum, Number max, Number min,
                                       Number sum_squares) {
        this.name = notNull(name);
        this.count = count == null ? 0 : count;
        this.sum = sum == null ? 0 : numeric(sum);
        this.max = numeric(max);
        this.min = numeric(min);
        this.sum_squares = numeric(sum_squares);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Number> toMap() {
        Map<String, Number> result = new HashMap<String, Number>(5);
        result.put("count", count);
        result.put("sum", sum);
        if (max != null) {
            result.put("max", max);
        }
        if (min != null) {
            result.put("min", min);
        }
        if (sum_squares != null) {
            result.put("sum_squares", sum_squares);
        }
        return result;
    }
}
