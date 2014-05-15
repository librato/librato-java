package com.librato.metrics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.librato.metrics.HttpPoster.Response;
import static org.junit.Assert.assertEquals;
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
    public void testPostsACounter() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final long epoch = System.currentTimeMillis();
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
        final long epoch = System.currentTimeMillis();
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
    public void testPostsAGauge() throws Exception {
        final Response response = new FakeResponse(200);
        final Future<Response> future = ReturningFuture.of(response);
        Mockito.when(poster.post(anyString(), anyString())).thenReturn(future);
        final long epoch = System.currentTimeMillis();
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
        final long epoch = System.currentTimeMillis();
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
}

