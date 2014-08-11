package com.librato.metrics;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ning.http.client.AsyncHttpClient;

public class NingHttpGetter implements HttpGetter {
	
	 private final AsyncHttpClient httpClient = new AsyncHttpClient();
	 private final String authHeader;
	 private final String apiUrl;

	    public NingHttpGetter(String authHeader, String apiUrl) {
	        this.authHeader = authHeader;
	        this.apiUrl = apiUrl;
	    }

	    /**
	     * Return a new poster with the correct authentication header.
	     *
	     * @param username the librato username
	     * @param token    the librato token
	     * @param apiUrl   the URL to post to
	     * @return a new NingHttpPoster
	     */
	    public static NingHttpGetter newGetter(String username, String token, String apiUrl, String queryString) {
	        apiUrl = queryString ==  null ? "" : (apiUrl + queryString);
	    	return new NingHttpGetter(Authorization.buildAuthHeader(username, token),apiUrl);
	    }

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
	    
		public Future<Response> get(String userAgent)
				throws IOException {			
			final AsyncHttpClient.BoundRequestBuilder builder = httpClient.prepareGet(apiUrl);
	    	builder.addHeader("Authorization", authHeader);
	    	builder.addHeader("User-Agent", userAgent);
	    	builder.addHeader("Content-Type", "application/json");
	    	return new FutureAdapter(builder.execute());
		}
		

}
