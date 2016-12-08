package com.librato.metrics.client;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bundle of measures
 */
public class Measures {
    private final String source;
    private final Long epoch;
    private final Integer period;
    private final List<IMeasure> measures = new LinkedList<IMeasure>();
    private final List<Tag> tags = new LinkedList<Tag>();

    public Measures() {
        this.source = null;
        this.epoch = null;
        this.period = null;
    }

    public Measures(String source, Long epoch, Integer period) {
        this.source = source;
        this.epoch = epoch;
        this.period = period;
    }

    public Measures(Measures measures, List<IMeasure> batch) {
        this.source = measures.source;
        this.epoch = measures.epoch;
        this.period = measures.period;
        this.measures.addAll(batch);
        this.tags.addAll(measures.tags);
    }

    public List<Measures> partition(int size) {
        List<Measures> result = new LinkedList<Measures>();
        for (List<IMeasure> batch : Lists.partition(this.measures, size)) {
            result.add(new Measures(this, batch));
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

    public List<Tag> getTags() {
        return tags;
    }

    interface MeasurePredicate {
        boolean accept(IMeasure measure);
    }

    private Measures convert(MeasurePredicate predicate) {
        Measures result = new Measures(source, epoch, period);
        for (IMeasure measure : this.measures) {
            if (predicate.accept(measure)) {
                result.measures.add(measure);
            }
        }
        return result;
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

    public Integer getPeriod() {
        return period;
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
