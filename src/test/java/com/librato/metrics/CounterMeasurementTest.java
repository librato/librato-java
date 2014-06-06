package com.librato.metrics;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class CounterMeasurementTest {
    @Test
    public void testAllowNullSource() {
        new CounterMeasurement(null, "my.fancy.counter", 17L);
    }

    @Test
    public void testAllowSource() {
        new CounterMeasurement("my.fancy.source", "my.fancy.counter", 17L);
    }

    @Test
    public void testAllowNullPeriod() {
      new CounterMeasurement("foo", null, "my.fancy.counter", 17L);
    }

    @Test
    public void testAllowPeriod() {
      new CounterMeasurement("my.fancy.source", 60L, "my.fancy.counter", 17L);
    }


  @Test
    public void testCorrectMap() throws Exception {
        CounterMeasurement counterMeasurement = new CounterMeasurement("my.fancy.counter", 17L);
        Map<String, Number> map = counterMeasurement.toMap();

        assertEquals(1, map.size());
        assertEquals(17L, map.get("value"));
    }
}
