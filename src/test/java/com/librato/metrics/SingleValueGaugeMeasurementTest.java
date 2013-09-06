package com.librato.metrics;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SingleValueGaugeMeasurementTest {
    @Test
    public void testCorrectMap() throws Exception {
        SingleValueGaugeMeasurement single = new SingleValueGaugeMeasurement("my.fancy.gauge", 45L);
        Map<String, Number> map = single.toMap();
        assertEquals(1, map.size());
        assertEquals(45L, map.get("value"));
    }
}
