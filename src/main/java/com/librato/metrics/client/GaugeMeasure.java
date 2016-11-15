package com.librato.metrics.client;

import java.util.Map;

public class GaugeMeasure extends AbstractMeasure {
    private String source;
    private Double value;
    private Double sum;
    private Double min;
    private Double max;
    private Double sumSquares;
    private Long count;

    public GaugeMeasure(String name, double value) {
        super(name);
        this.value = value;
    }

    public GaugeMeasure(String name, double sum, long count, double min, double max) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
    }

    public GaugeMeasure(String name, double sum, long count, double min, double max, double sumSquares) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
        this.sumSquares = sumSquares;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        Maps.putIfNotNull(map, "measure_time", epoch);
        Maps.putIfNotNull(map, "source", Sanitizer.LAST_PASS.apply(source));
        Maps.putIfNotNull(map, "value", value);
        Maps.putIfNotNull(map, "sum", sum);
        Maps.putIfNotNull(map, "count", count);
        Maps.putIfNotNull(map, "min", min);
        Maps.putIfNotNull(map, "max", max);
        Maps.putIfNotNull(map, "sum_squares", sumSquares);
        return map;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public boolean isGauge() {
        return true;
    }

    public GaugeMeasure setTime(long epochSeconds) {
        this.epoch = epochSeconds;
        return this;
    }

    public GaugeMeasure setSource(String source) {
        this.source = source;
        return this;
    }

    public GaugeMeasure setMetricAttributes(Map<String, Object> attributes) {
        this.metricAttributes = attributes;
        return this;
    }

    public GaugeMeasure setPeriod(int period) {
        this.period = period;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Double getValue() {
        return value;
    }

    public Double getSum() {
        return sum;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getSumSquares() {
        return sumSquares;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GaugeMeasure that = (GaugeMeasure) o;

        if (source != null ? !source.equals(that.source) : that.source != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;
        if (sum != null ? !sum.equals(that.sum) : that.sum != null)
            return false;
        if (min != null ? !min.equals(that.min) : that.min != null)
            return false;
        if (max != null ? !max.equals(that.max) : that.max != null)
            return false;
        if (sumSquares != null ? !sumSquares.equals(that.sumSquares) : that.sumSquares != null)
            return false;
        return count != null ? count.equals(that.count) : that.count == null;

    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (sum != null ? sum.hashCode() : 0);
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (sumSquares != null ? sumSquares.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("name='").append(name).append('\'');
        sb.append(", source='").append(source).append('\'');
        sb.append(", value=").append(value);
        sb.append(", sum=").append(sum);
        sb.append(", min=").append(min);
        sb.append(", max=").append(max);
        sb.append(", sumSquares=").append(sumSquares);
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
