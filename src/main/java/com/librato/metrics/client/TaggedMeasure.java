package com.librato.metrics.client;

import java.util.*;

public class TaggedMeasure extends AbstractMeasure {
    private double sum;
    private int count;
    private double min;
    private double max;
    private List<Tag> tags = new LinkedList<Tag>();

    public TaggedMeasure(String name, double value, Tag tag, Tag...tags) {
        this(name, value, 1, value, value, tag, tags);
    }

    public TaggedMeasure(String name, double sum, int count, double min, double max, Tag tag, Tag...tags) {
        super(name);
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
        this.tags.add(tag);
        Collections.addAll(this.tags, tags);
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


}
