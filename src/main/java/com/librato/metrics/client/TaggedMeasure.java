package com.librato.metrics.client;

import java.util.*;

public class TaggedMeasure extends AbstractMeasure {
    private double sum;
    private long count;
    private double min;
    private double max;
    private List<Tag> tags = new LinkedList<Tag>();

    public TaggedMeasure(GaugeMeasure measure) {
        super(measure);
        if (measure.getValue() != null) {
            this.sum = measure.getValue();
            this.count = 1;
            this.min = sum;
            this.max = sum;
        } else {
            this.sum = measure.getSum();
            this.count = measure.getCount();
            this.min = measure.getMin();
            this.max = measure.getMax();
        }
    }

    public TaggedMeasure(String name, double value, Tag tag, Tag...tags) {
        this(name, value, 1, value, value, tag, tags);
    }

    public TaggedMeasure(String name, double sum, long count, double min, double max, Tag tag, Tag...tags) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
        this.tags.add(tag);
        Collections.addAll(this.tags, tags);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        Maps.putIfNotNull(map, "time", epoch);
        map.put("sum", sum);
        map.put("count", count);
        map.put("min", min);
        map.put("max", max);
        if (!tags.isEmpty()) {
            map.put("tags", Tags.toMap(tags));
        }
        return map;
    }

    @Override
    public boolean isTagged() {
        return true;
    }

    @Override
    public boolean isGauge() {
        return true;
    }

    public TaggedMeasure setPeriod(int period) {
        this.period = period;
        return this;
    }

    public TaggedMeasure setMetricAttributes(Map<String, Object> attributes) {
        this.metricAttributes = attributes;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("name=").append(name);
        sb.append(", epoch=").append(epoch);
        sb.append(", tags=").append(tags);
        sb.append(", sum=").append(sum);
        sb.append(", count=").append(count);
        sb.append(", min=").append(min);
        sb.append(", max=").append(max);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TaggedMeasure that = (TaggedMeasure) o;

        if (Double.compare(that.sum, sum) != 0) return false;
        if (count != that.count) return false;
        if (Double.compare(that.min, min) != 0) return false;
        if (Double.compare(that.max, max) != 0) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(sum);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (count ^ (count >>> 32));
        temp = Double.doubleToLongBits(min);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}
