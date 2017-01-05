package com.librato.metrics.client;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LibratoClientTest {
    String metricsUrl = "https://metrics-api.librato.com/v1/metrics";
    String measuresUrl = "https://metrics-api.librato.com/v1/measurements";
    Duration connectTimeout = new Duration(5, TimeUnit.SECONDS);
    Duration timeout = new Duration(10, TimeUnit.SECONDS);
    Map<String, String> headers = new HashMap<String, String>();
    FakePoster poster = new FakePoster();
    LibratoClient client = LibratoClient.builder("foo@example.com", "token")
            .setPoster(poster)
            .setAgentIdentifier("test-lib")
            .setBatchSize(2)
            .build();

    @Before
    public void setUp() throws Exception {
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", Authorization.buildAuthHeader("foo@example.com", "token"));
        headers.put("User-Agent", "test-lib librato-java/0.0.10");
    }

    @Test
    public void testTrimsTagName() {
        long now = System.currentTimeMillis() / 1000;
        client.postMeasures(new Measures(null, Collections.<Tag>emptyList(), now)
                .add(new TaggedMeasure("metric-name", 42,
                        new Tag("tagNametagNametagNametagNametagNametagNametagNametagNametagNameta", // 65 ch
                                "tagValue"))));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(measuresUrl, connectTimeout, timeout, headers, new Payload()
                        .setTime(now)
                        .addTagged("metric-name", 42,
                                new Tag("tagNametagNametagNametagNametagNametagNametagNametagNametagNamet", // 64 ch
                                        "tagValue")))));
    }

    @Test
    public void testTrimsTagValue() throws Exception {
        long now = System.currentTimeMillis() / 1000;
        client.postMeasures(new Measures(null, Collections.<Tag>emptyList(), now)
                .add(new TaggedMeasure("metric-name", 42,
                        new Tag("tagName",
                                "tagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValue")))); // 256
        assertThat(poster.posts).isEqualTo(asList(
                new Post(measuresUrl, connectTimeout, timeout, headers, new Payload()
                        .setTime(now)
                        .addTagged("metric-name", 42,
                                new Tag("tagName",
                                        "tagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValuetagValu"))))); // 255
    }

    @Test
    public void testSendsPeriod() throws Exception {
        long now = System.currentTimeMillis() / 1000;
        client.postMeasures(new Measures("foo", Collections.<Tag>emptyList(), now, 60)
                .add(new GaugeMeasure("foo", 42).setPeriod(30)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .setMeasureTime(now)
                        .setSource("foo")
                        .setPeriod(60)
                        .addGauge("foo", 42, 30))));
    }

    @Test
    public void testSplitsPayloads() throws Exception {
        client.postMeasures(new Measures()
                .add(new GaugeMeasure("foo", 42))
                .add(new GaugeMeasure("bar", 43))
                .add(new GaugeMeasure("split", 45)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .addGauge("foo", 42)
                        .addGauge("bar", 43)),
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .addGauge("split", 45))));
    }

    @Test
    public void testPostsBothSDAndMDMeasures() throws Exception {
        client.postMeasures(new Measures()
                .add(new TaggedMeasure("foo", 42, new Tag("x", "y")))
                .add(new GaugeMeasure("bar", 43)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .addGauge("bar", 43)),
                new Post(measuresUrl, connectTimeout, timeout, headers, new Payload()
                        .addTagged("foo", 42, new Tag("x", "y")))));

    }

    @Test
    public void testPostsATaggedMeasure() throws Exception {
        client.postMeasures(new Measures()
                .add(new TaggedMeasure("foo", 42, new Tag("x", "y"))));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(measuresUrl, connectTimeout, timeout, headers, new Payload()
                        .addTagged("foo", 42, new Tag("x", "y")))));

    }

    @Test
    public void testPostsBothCounterAndGauge() throws Exception {
        client.postMeasures(new Measures()
                .add(new CounterMeasure("foo", 5))
                .add(new GaugeMeasure("bar", 6)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .addCounter("foo", 5)
                        .addGauge("bar", 6))));

    }

    @Test
    public void testPostsCounter() throws Exception {
        client.postMeasures(new Measures()
                .add(new CounterMeasure("foo", 5)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .addCounter("foo", 5))));

    }

    @Test
    public void testPostsComplexGauge() throws Exception {
        client.postMeasures(new Measures()
                .add(new GaugeMeasure("foo", 42, 2, 0, 42, 1.4)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .addGauge("foo", 42, 2, 0, 42, 1.4))));
    }

    @Test
    public void testPostsGauge() throws Exception {
        client.postMeasures(new Measures()
                .add(new GaugeMeasure("foo", 42)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(metricsUrl, connectTimeout, timeout, headers, new Payload()
                        .addGauge("foo", 42))));
    }

    @Test
    public void testNothingPosts() throws Exception {
        FakePoster poster = new FakePoster();
        LibratoClient client = LibratoClient.builder("foo@example.com", "token")
                .setPoster(poster)
                .build();
        PostMeasuresResult result = client.postMeasures(new Measures());
        assertThat(result.results).isEmpty();
        assertThat(poster.posts).isEmpty();
    }

    @Test
    public void testVerifiesEmailAndToken() throws Exception {
        ensureIllegalArgument(new Runnable() {
            @Override
            public void run() {
                LibratoClient.builder(null, null).build();
            }
        });
        ensureIllegalArgument(new Runnable() {
            @Override
            public void run() {
                LibratoClient.builder("foo@example.com", null).build();
            }
        });
        ensureIllegalArgument(new Runnable() {
            @Override
            public void run() {
                LibratoClient.builder(null, "token").build();
            }
        });
        LibratoClient.builder("foo@example.com", "token").build();
    }

    private void ensureIllegalArgument(Runnable runnable) {
        try {
            runnable.run();
            Assertions.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
