package com.librato.metrics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.librato.metrics.HttpPoster.Response;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

public class LibratoBatchTest {
    final String source = "junit";
    final String agent = "test-agent";

    HttpPoster poster;

    @Before
    public void setUp() throws Exception {
        poster = Mockito.mock(HttpPoster.class);
    }

    @Test
    public void testIsOkWithNoEpochSet() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addGaugeMeasurement("apples", 1L);
        batch.post(source); // no epoch set

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(0, payload.getCounters().size());
        assertEquals(1, payload.getGauges().size());
        assertEquals(Gauge.of("apples", 1), payload.getGauges().iterator().next());
        assertNull(payload.getMeasureTime());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReturnsBatchResponseOn200() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addCounterMeasurement("apples", 1L);
        BatchResult result = batch.post(source, epoch);
        assertEquals(1, result.getPosts().size());
        PostResult postResult = result.getPosts().iterator().next();
        assertEquals(200, postResult.getStatusCode().intValue());
        assertNull(postResult.getException());
        assertEquals(1, ((List<Map<String, Object>>) postResult.getData().get("counters")).size());
        assertEquals(0, ((List<Map<String, Object>>) postResult.getData().get("gauges")).size());
        assertTrue(result.success());
        assertEquals(0, result.getFailedPosts().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReturnsBatchResponseOnException() throws Exception {
        Mockito.when(poster.post(anyString(), anyString())).thenThrow(new RuntimeException("test-failure"));
        final long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addGaugeMeasurement("apples", 1L);
        BatchResult result = batch.post(source, epoch);
        assertEquals(1, result.getPosts().size());
        PostResult postResult = result.getPosts().iterator().next();
        assertNull(postResult.getStatusCode());
        assertNotNull(postResult.getException());
        assertEquals(0, ((List<Map<String, Object>>) postResult.getData().get("counters")).size());
        assertEquals(1, ((List<Map<String, Object>>) postResult.getData().get("gauges")).size());
        assertFalse(result.success());
        assertEquals(1, result.getFailedPosts().size());
    }

    @Test
    public void testPostsACounter() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addCounterMeasurement("apples", 1L);
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(1, payload.getCounters().size());
        assertEquals(0, payload.getGauges().size());
        assertEquals(Counter.of("apples", 1), payload.getCounters().iterator().next());
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsACounterWithSource() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addCounterMeasurement("farm", "apples", 1L);
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(1, payload.getCounters().size());
        assertEquals(0, payload.getGauges().size());
        assertEquals(Counter.of("farm", "apples", 1), payload.getCounters().iterator().next());
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsACounterWithPeriod() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addCounterMeasurement("farm", 60L, "apples", 1L);
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(1, payload.getCounters().size());
        assertEquals(0, payload.getGauges().size());
        assertEquals(Counter.of("farm", 60, "apples", 1), payload.getCounters().iterator().next());
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsAGauge() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addGaugeMeasurement("apples", 1L);
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(0, payload.getCounters().size());
        assertEquals(1, payload.getGauges().size());
        assertEquals(Gauge.of("apples", 1), payload.getGauges().iterator().next());
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsAGaugeWithSource() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addGaugeMeasurement("farm", "apples", 1L);
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(0, payload.getCounters().size());
        assertEquals(1, payload.getGauges().size());
        assertEquals(Gauge.of("farm", "apples", 1), payload.getGauges().iterator().next());
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsAGaugeWithPeriod() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addGaugeMeasurement("farm", 60, "apples", 1L);
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(0, payload.getCounters().size());
        assertEquals(1, payload.getGauges().size());
        assertEquals(Gauge.of("farm", 60, "apples", 1), payload.getGauges().iterator().next());
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsAnAggregatedGauge() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addMeasurement(SingleValueGaugeMeasurement
                .builder("farm", 60)
                .setMetricAttribute("aggregate", true) // add the metric attribute
                .build());
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(0, payload.getCounters().size());
        assertEquals(1, payload.getGauges().size());
        Gauge gauge = payload.getGauges().iterator().next();
        assertEquals(1, gauge.attributes.size());
        assertEquals(true, gauge.attributes.get("aggregate"));
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsAnAggregatedMultiSampleGauge() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addMeasurement(MultiSampleGaugeMeasurement
                .builder("farm")
                .setCount(1L)
                .setSum(50L)
                .setMetricAttribute("aggregate", true) // add the metric attribute
                .build());
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(0, payload.getCounters().size());
        assertEquals(1, payload.getGauges().size());
        Gauge gauge = payload.getGauges().iterator().next();
        assertEquals(1, gauge.attributes.size());
        assertEquals(true, gauge.attributes.get("aggregate"));
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testPostsAnAggregatedCounter() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addMeasurement(CounterMeasurement
                .builder("farm", 60L)
                .setMetricAttribute("aggregate", true) // add the metric attribute
                .build());
        batch.post(source, epoch);

        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(1, payload.getCounters().size());
        assertEquals(0, payload.getGauges().size());
        Counter counter = payload.getCounters().iterator().next();
        assertEquals(1, counter.attributes.size());
        assertEquals(true, counter.attributes.get("aggregate"));
        assertEquals(epoch, payload.getMeasureTime());
    }

    @Test
    public void testDoesNotPostIfNoData() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(1, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        // don't add anything
        batch.post("junit", epoch);
        Mockito.verify(poster, Mockito.times(0)).post(anyString(), anyString());
    }

    @Test
    public void testAllowsMeasureTimeOverride() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final Long epoch = System.currentTimeMillis();
        final LibratoBatch batch = new LibratoBatch(100, Sanitizer.NO_OP, 1, TimeUnit.SECONDS, agent, poster);
        batch.addMeasurement(SingleValueGaugeMeasurement.builder("test-gauge", 42).setMeasureTime(1042L).build());
        batch.addMeasurement(MultiSampleGaugeMeasurement.builder("multi-sample-gauge").setSum(42).setCount(1L).setMeasureTime(1043L).build());
        batch.addMeasurement(SingleValueGaugeMeasurement.builder("test-gauge-no-measure-time", 42).build());
        batch.addMeasurement(CounterMeasurement.builder("test-counter", 42L).setMeasureTime(1044L).build());

        batch.post(source, epoch);
        ArgumentCaptor<String> payloadCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(poster).post(Matchers.eq("test-agent librato-java/unknown"), payloadCapture.capture());
        final Payload payload = Payload.parse(payloadCapture.getValue());
        assertEquals(source, payload.getSource());
        assertEquals(1, payload.getCounters().size());
        Counter counter = payload.getCounters().get(0);
        assertEquals(new Long(1044), counter.measureTime);
        assertEquals(3, payload.getGauges().size());
        for (Gauge gauge : payload.getGauges()) {
            if (gauge.getName().equals("test-gauge")) {
                assertEquals(new Long(1042), gauge.measureTime);
            } else if (gauge.getName().equals("multi-sample-gauge")) {
                assertEquals(new Long(1043), gauge.measureTime);
            } else if (gauge.getName().equals("test-gauge-no-measure-time")) {
                assertNull(gauge.measureTime);
            } else {
                throw new Exception("Unexpected gauge: " + gauge.getName());
            }
        }
        assertEquals(epoch, payload.getMeasureTime());
    }
}

