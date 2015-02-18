package com.librato.metrics;

import java.util.HashMap;
import java.util.Map;

import static com.librato.metrics.Preconditions.checkNotNull;
import static com.librato.metrics.Preconditions.checkNumeric;

/**
 * A class for representing a gauge reading that might come from multiple samples
 * <p/>
 * See http://dev.librato.com/v1/post/metrics for why some fields are optional
 */
public class MultiSampleGaugeMeasurement implements Measurement {
    private final Number period;
    private final String source;
    private final String name;
    private final Long count;
    private final Number sum;
    private final Number max;
    private final Number min;
    private final Number sumSquares;
    private final Map<String, Object> metricAttributes;
    private final Long measureTime;

    public static MultiSampleGaugeMeasurementBuilder builder(String name) {
        return new MultiSampleGaugeMeasurementBuilder(name);
    }

    public MultiSampleGaugeMeasurement(String source,
                                       Number period,
                                       String name,
                                       Long count,
                                       Number sum,
                                       Number max,
                                       Number min,
                                       Number sumSquares,
                                       Map<String, Object> metricAttributes,
                                       Long measureTime) {
        try {
            if (count == null || count == 0) {
                throw new IllegalArgumentException("The Librato API requires the count to be > 0 for complex metrics. See http://dev.librato.com/v1/post/metrics");
            }
            this.source = source;
            this.period = period;
            this.name = checkNotNull(name);
            this.count = count;
            this.sum = checkNumeric(sum);
            this.max = checkNumeric(max);
            this.min = checkNumeric(min);
            this.sumSquares = checkNumeric(sumSquares);
            this.metricAttributes = metricAttributes;
            this.measureTime = measureTime;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid multi-sample gauge measurement name=" + name, e);
        }
    }

    public Long getMeasureTime() {
        return measureTime;
    }

    public Map<String, Object> getMetricAttributes() {
        return metricAttributes;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public Number getPeriod() { return period; }

    public Map<String, Number> toMap() {
        final Map<String, Number> result = new HashMap<String, Number>(5);
        result.put("count", count);
        result.put("sum", sum);
        if (max != null) {
            result.put("max", max);
        }
        if (min != null) {
            result.put("min", min);
        }
        if (sumSquares != null) {
            result.put("sum_squares", sumSquares);
        }
        return result;
    }
}
