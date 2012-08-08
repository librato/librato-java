package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;


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
        this.name = name;
        this.count = count;
        this.sum = numeric("sum", sum);
        this.max = numeric("max", max);
        this.min = numeric("max", min);
        this.sum_squares = numeric("max", sum_squares);
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

    private Number numeric(String name, Number number) {
        if (number == null) {
            return null;
        }

        if (isNaN(number.doubleValue()) || isInfinite(number.doubleValue())) {
            throw new IllegalArgumentException(number + " is not a numeric value for " + name);
        }

        return number;
    }
}
