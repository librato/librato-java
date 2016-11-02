package com.librato.metrics.client;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bundle of measures
 */
public class Measures {
    private final Long epoch;
    private final List<IMeasure> measures = new LinkedList<IMeasure>();

    public Measures() {
        this(null);
    }

    public Measures(Long epoch) {
        this.epoch = epoch;
    }

    public Measures add(GaugeMeasure measure) {
        this.measures.add(measure);
        return this;
    }

    public Long getEpoch() {
        return epoch;
    }

    public List<IMeasure> getMeasures() {
        return measures;
    }

    public boolean isEmpty() {
        return measures.isEmpty();
    }
}
