package com.librato.metrics;

import org.junit.Test;

/**
 * Verify that all the advanced details are filled in that are available
 */
public class MultiSampleGaugeMeasurementTest {
    @Test
    public void testAllowNullSource() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource(null)
                .setPeriod(60L)
                .setCount(2L)
                .setSum(2)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test
    public void testAllowSource() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60L)
                .setCount(2L)
                .setSum(2)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test
    public void testAllowNullPeriod() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(null)
                .setCount(2L)
                .setSum(2)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test
    public void testAllowPeriod() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(2L)
                .setSum(2)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNaNAsSum() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(2L)
                .setSum(Double.NaN)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptPosInfAsSum() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(2L)
                .setSum(Double.POSITIVE_INFINITY)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNegInfAsSum() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(2L)
                .setSum(Double.NEGATIVE_INFINITY)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNonNumericValuesAsMax() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(2L)
                .setSum(2L)
                .setMax(Double.NEGATIVE_INFINITY)
                .setMin(2)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNonNumericValuesAsMin() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(2L)
                .setSum(2L)
                .setMax(2)
                .setMin(Double.NEGATIVE_INFINITY)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNonNumericValuesAsSumSquares() {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(2L)
                .setSum(2L)
                .setMax(2)
                .setMin(2)
                .setSumSquares(Double.NEGATIVE_INFINITY)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullCount() throws Exception {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(null)
                .setSum(2L)
                .setMax(2)
                .setMin(2)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptZeroCount() throws Exception {
        new MultiSampleGaugeMeasurementBuilder("name")
                .setSource("source")
                .setPeriod(60)
                .setCount(0L)
                .setSum(2L)
                .setMax(2)
                .setMin(2)
                .build();
    }
}
