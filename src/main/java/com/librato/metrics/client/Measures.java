package com.librato.metrics.client;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bundle of measures
 */
public class Measures {
    private final String source;
    private final Long epoch;
    private final List<IMeasure> measures = new LinkedList<IMeasure>();

    public Measures() {
        this(null);
    }

    public Measures(Long epoch) {
        this(null, epoch);
    }

    public Measures(String source, Long epoch) {
        this.source = source;
        this.epoch = epoch;
    }

    public Measures(String source, Long epoch, List<IMeasure> batch) {
        this(source, epoch);
        this.measures.addAll(batch);
    }

    public List<Measures> partition(int size) {
        List<Measures> result = new LinkedList<Measures>();
        for (List<IMeasure> batch : Lists.partition(this.measures, size)) {
            result.add(new Measures(source, epoch, batch));
        }
        return result;
    }

    public Measures toSD() {
        return convert(new MeasurePredicate() {
            @Override
            public boolean accept(IMeasure measure) {
                return !measure.isTagged();
            }
        });
    }

    public Measures toMD() {
        return convert(new MeasurePredicate() {
            @Override
            public boolean accept(IMeasure measure) {
                return measure.isTagged();
            }
        });
    }

    public String getSource() {
        return source;
    }

    interface MeasurePredicate {
        boolean accept(IMeasure measure);
    }

    private Measures convert(MeasurePredicate predicate) {
        Measures measures = new Measures(source, epoch);
        for (IMeasure measure : this.measures) {
            if (predicate.accept(measure)) {
                measures.measures.add(measure);
            }
        }
        return measures;
    }

    public Measures add(TaggedMeasure measure) {
        return addMeasure(measure);
    }

    public Measures add(CounterMeasure measure) {
        return addMeasure(measure);
    }

    public Measures add(GaugeMeasure measure) {
        return addMeasure(measure);
    }

    private Measures addMeasure(IMeasure measure) {
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
