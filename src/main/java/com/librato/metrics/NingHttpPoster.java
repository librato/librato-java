package com.librato.metrics;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An HTTPPoster which uses ning's HTTP client
 */
@SuppressWarnings("unused")
public class NingHttpPoster implements HttpPoster {
    private final AsyncHttpClient httpClient;
    private final String authHeader;
    private final String apiUrl;

    public NingHttpPoster(String authHeader, String apiUrl) {
        this.authHeader = authHeader;
        this.apiUrl = apiUrl;
        this.httpClient = new AsyncHttpClient();
    }

    public NingHttpPoster(String authHeader, String apiUrl, AsyncHttpClientConfig httpClientConfig) {
        this.authHeader = authHeader;
        this.apiUrl = apiUrl;
        this.httpClient = new AsyncHttpClient(httpClientConfig);
    }

    /**
     * Return a new poster with the correct authentication header.
     *
     * @param username the librato username
     * @param token    the librato token
     * @param apiUrl   the URL to post to
     * @return a new NingHttpPoster
     */
    public static NingHttpPoster newPoster(String username, String token, String apiUrl) {
        return new NingHttpPoster(Authorization.buildAuthHeader(username, token), apiUrl);
    }

    /**
     * Return a new poster with the correct authentication header.
     *
     * @param username         the librato username
     * @param token            the librato token
     * @param apiUrl           the URL to post to
     * @param httpClientConfig configuration of http client, this can be use to pass a proxy server.
     * @return a new NingHttpPoster
     */
    public static NingHttpPoster newPoster(String username, String token, String apiUrl, AsyncHttpClientConfig httpClientConfig) {
        return new NingHttpPoster(Authorization.buildAuthHeader(username, token), apiUrl, httpClientConfig);
    }

    /**
     * Adapts a ning {@link com.ning.http.client.Response} future to a @{link Response} future
     */
    static class FutureAdapter implements Future<Response> {
        private final Future<com.ning.http.client.Response> delegate;

        FutureAdapter(Future<com.ning.http.client.Response> delegate) {
            this.delegate = delegate;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            return delegate.cancel(mayInterruptIfRunning);
        }

        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        public boolean isDone() {
            return delegate.isDone();
        }

        public Response get() throws InterruptedException, ExecutionException {
            return adapt(delegate.get());
        }


        public Response get(long timeout, @SuppressWarnings("NullableProblems") TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return adapt(delegate.get(timeout, unit));
        }

        private Response adapt(final com.ning.http.client.Response response) {
            return new Response() {
                public int getStatusCode() {
                    return response.getStatusCode();
                }

                public String getBody() throws IOException {
                    return response.getResponseBody();
                }
            };
        }
    }

    public Future<Response> post(String userAgent, String payload) throws IOException {
        final AsyncHttpClient.BoundRequestBuilder builder = httpClient.preparePost(apiUrl);
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Authorization", authHeader);
        builder.addHeader("User-Agent", userAgent);
        builder.setBody(payload);
        return new FutureAdapter(builder.execute());
    }

    public void close() throws IOException {
      httpClient.close();
    }
}
