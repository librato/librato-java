package com.librato.metrics;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class CounterMeasurementTest {
    @Test
    public void testAllowNullSource() {
        new CounterMeasurementBuilder("my.fancy.counter", 17L)
                .setSource(null)
                .build();
    }

    @Test
    public void testAllowSource() {
        new CounterMeasurementBuilder("my.fancy.counter", 17L)
                .setSource("my.fancy.source")
                .build();
    }

    @Test
    public void testAllowNullPeriod() {
        new CounterMeasurementBuilder("my.fancy.counter", 17L)
                .setSource("foo")
                .setPeriod(null)
                .build();
    }

    @Test
    public void testAllowPeriod() {
        new CounterMeasurementBuilder("my.fancy.counter", 17L)
                .setSource("foo")
                .setPeriod(60L)
                .build();
    }


    @Test
    public void testCorrectMap() throws Exception {
        CounterMeasurement counterMeasurement = new CounterMeasurementBuilder("my.fancy.counter", 17L).build();
        Map<String, Number> map = counterMeasurement.toMap();

        assertEquals(1, map.size());
        assertEquals(17L, map.get("value"));
    }
}
