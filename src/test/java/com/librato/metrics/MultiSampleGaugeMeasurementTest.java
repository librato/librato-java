package com.librato.metrics;

import org.junit.Test;

/**
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

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullCount() throws Exception {
        Measurement m = new MultiSampleGaugeMeasurement("testGauge", null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptZeroCount() throws Exception {
        Measurement m = new MultiSampleGaugeMeasurement("testGauge", 0L, null, null, null, null);
    }
}
