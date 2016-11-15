package com.librato.metrics.client;

import com.librato.metrics.Authorization;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LibratoClientTest {
    String uri = "https://metrics-api.librato.com/v1/metrics";
    Duration connectTimeout = new Duration(5, TimeUnit.SECONDS);
    Duration timeout = new Duration(10, TimeUnit.SECONDS);
    Map<String, String> headers = new HashMap<String, String>();

    @Before
    public void setUp() throws Exception {
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", Authorization.buildAuthHeader("foo@example.com", "token"));
        headers.put("User-Agent", "unknown librato-java/unknown");
    }

    @Test
    public void testPostsGauge() throws Exception {
        FakePoster poster = new FakePoster();
        LibratoClient client = LibratoClient.builder("foo@example.com", "token")
                .setPoster(poster)
                .build();
        client.postMeasures(new Measures()
                .add(new GaugeMeasure("foo", 42)));
        assertThat(poster.posts).isEqualTo(asList(
                new Post(uri, connectTimeout, timeout, headers, new SDPayload()
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
