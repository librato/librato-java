package com.librato.metrics.client;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bundle of measures
 */
public class Measures {
    private final long epoch;
    private final List<IMeasure> measures = new LinkedList<IMeasure>();

    public Measures() {
        this.epoch = System.currentTimeMillis() / 1000;
    }

    public Measures(long epoch) {
        this.epoch = epoch;
    }

    public Measures addMeasure(IMeasure measure) {
        this.measures.add(measure);
        return this;
    }

}
