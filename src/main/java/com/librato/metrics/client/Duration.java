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

    @Override
    public String toString() {
        return duration + "" + timeUnit.toString().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Duration duration1 = (Duration) o;

        if (duration != duration1.duration) return false;
        return timeUnit == duration1.timeUnit;

    }

    @Override
    public int hashCode() {
        int result = (int) (duration ^ (duration >>> 32));
        result = 31 * result + (timeUnit != null ? timeUnit.hashCode() : 0);
        return result;
    }
}
