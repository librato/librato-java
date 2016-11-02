package com.librato.metrics.client;

import java.util.concurrent.TimeUnit;

public class Duration {
    public final long duration;
    public final TimeUnit timeUnit;

    public Duration(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public long to(TimeUnit timeUnit) {
        return timeUnit.convert(this.duration, this.timeUnit);
    }
}
