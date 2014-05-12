package com.librato.metrics;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SanitizerTest extends TestCase {

    public void testRemovesIllegalCharacters() throws Exception {
        assertThat(Sanitizer.LAST_PASS.apply("foo##bar"), equalTo("foobar"));
    }

    public void testLastPassAllowsDashes() throws Exception {
        assertThat(Sanitizer.LAST_PASS.apply("my-metric-name"), equalTo("my-metric-name"));
    }

    public void testDotsOk() throws Exception {
        assertThat(Sanitizer.LAST_PASS.apply("my.metric.name"), equalTo("my.metric.name"));
    }

    public void testUnderscoresOk() throws Exception {
        assertThat(Sanitizer.LAST_PASS.apply("my_metric_name"), equalTo("my_metric_name"));
    }
}
