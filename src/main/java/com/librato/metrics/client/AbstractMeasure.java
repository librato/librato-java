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
        map.put("name", Sanitizer.METRIC_NAME_SANITIZER.apply(name));
        Maps.putIfNotNull(map, "period", period);
        Maps.putIfNotEmpty(map, "attributes", metricAttributes);
        return map;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMeasure that = (AbstractMeasure) o;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (metricAttributes != null ? !metricAttributes.equals(that.metricAttributes) : that.metricAttributes != null)
            return false;
        if (period != null ? !period.equals(that.period) : that.period != null)
            return false;
        return epoch != null ? epoch.equals(that.epoch) : that.epoch == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (metricAttributes != null ? metricAttributes.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        result = 31 * result + (epoch != null ? epoch.hashCode() : 0);
        return result;
    }
}

