package com.librato.metrics.client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bundle of measures
 */
public class Measures {
    private final String source;
    private final Long epoch;
    private final List<IMeasure> measures = new LinkedList<IMeasure>();
    private final List<Tag> tags = new LinkedList<Tag>();

    public Measures() {
        this(null);
    }

    public Measures(Long epoch) {
        this(null, Collections.<Tag>emptyList(), epoch);
    }

    public Measures(String source, Long epoch) {
        this(source, Collections.<Tag>emptyList(), epoch);
    }

    public Measures(List<Tag> tags, Long epoch) {
        this(null, tags, epoch);
    }

    public Measures(String source, List<Tag> tags, Long epoch) {
        this(source, tags, epoch, Collections.<IMeasure>emptyList());
    }

    public Measures(String source, List<Tag> tags, Long epoch, List<IMeasure> batch) {
        this.source = source;
        this.epoch = epoch;
        this.measures.addAll(batch);
        this.tags.addAll(tags);
    }

    public List<Measures> partition(int size) {
        List<Measures> result = new LinkedList<Measures>();
        for (List<IMeasure> batch : Lists.partition(this.measures, size)) {
            result.add(new Measures(source, tags, epoch, batch));
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
