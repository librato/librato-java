package com.librato.metrics;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;


/**
 * User: mihasya
 * Date: 7/1/12
 * Time: 5:20 PM
 * Verify that all the advanced details are filled in that are available
 */
public class MultiSampleGaugeMeasurementTest {
    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNaNAsSum() {
        new MultiSampleGaugeMeasurement("testGauge", 12L, Double.NaN, 2, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptPosInfAsSum() {
        new MultiSampleGaugeMeasurement("testGauge", 12L, Double.POSITIVE_INFINITY, 2, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNegInfAsSum() {
        new MultiSampleGaugeMeasurement("testGauge", 12L, Double.NEGATIVE_INFINITY, 2, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNonNumericValuesAsMax() {
        new MultiSampleGaugeMeasurement("testGauge", 12L, 2, Double.NEGATIVE_INFINITY, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNonNumericValuesAsMin() {
        new MultiSampleGaugeMeasurement("testGauge", 12L, 2, 2, Double.POSITIVE_INFINITY, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNonNumericValuesAsSumSquares() {
        new MultiSampleGaugeMeasurement("testGauge", 12L, 2, 2, 2, Double.NaN);
    }

    @Test
    public void doesAcceptNullSumAndCount() throws Exception {
        Measurement m = new MultiSampleGaugeMeasurement("testGauge", null, null, null, null, null);
        Map<String, Number> map = m.toMap();
        assertEquals(Long.valueOf(0), (Long)map.get("count"));
        assertEquals(Integer.valueOf(0), (Integer)map.get("sum"));
    }
}
