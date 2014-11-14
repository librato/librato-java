package com.librato.metrics;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SingleValueGaugeMeasurementTest {
    @Test
    public void testAllowNullSource() {
        new SingleValueGaugeMeasurementBuilder("my.fancy.gauge", 17L)
                .setSource(null)
                .build();
    }

    @Test
    public void testAllowSource() {
        new SingleValueGaugeMeasurementBuilder("my.fancy.gauge", 17L)
                .setSource("my.fancy.source")
                .build();
    }

    @Test
    public void testCorrectMap() throws Exception {
        SingleValueGaugeMeasurement single = new SingleValueGaugeMeasurementBuilder("my.fancy.gauge", 45L).build();
        Map<String, Number> map = single.toMap();
        assertEquals(1, map.size());
        assertEquals(45L, map.get("value"));
    }
}
