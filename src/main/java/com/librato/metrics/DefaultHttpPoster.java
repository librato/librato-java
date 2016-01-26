package com.librato.metrics;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.*;

/**
 * Implementation of HttpPoster that just uses built-in stdlib classes to
 * reduce dependencies.
 */
public class DefaultHttpPoster implements HttpPoster {
    private final URL url;
    private final String authHeader;
    private final ExecutorService executor;

    public DefaultHttpPoster(String url, String email, String token) {
        this.authHeader = Authorization.buildAuthHeader(email, token);
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not parse URL", e);
        }
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(1);
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread();
                thread.setName("librato-http-poster");
                thread.setDaemon(true);
                return thread;
            }
        };
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        this.executor = new ThreadPoolExecutor(1, 1, 120, TimeUnit.SECONDS, queue, threadFactory, handler);
    }

    @Override
    public Future<Response> post(final String userAgent, final String payload) throws IOException {
        return executor.submit(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return doPost(userAgent, payload);
            }
        });
    }

    Response doPost(String userAgent, final String payload) throws IOException {
        HttpURLConnection connection = open(url);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Authorization", authHeader);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", userAgent);
        connection.connect();
        OutputStream outputStream = connection.getOutputStream();
        try {
            outputStream.write(payload.getBytes(Charset.forName("UTF-8")));
        } finally {
            close(outputStream);
        }
        final int responseCode = connection.getResponseCode();
        return new Response() {
            @Override
            public int getStatusCode() {
                return responseCode;
            }

            @Override
            public String getBody() throws IOException {
                return payload;
            }
        };
    }

    void close(OutputStream outputStream) {
        try {
            outputStream.close();
        } catch (IOException ignore) {
        }
    }

    HttpsURLConnection open(URL url) throws IOException {
        try {
            return (HttpsURLConnection) url.openConnection();
        } catch (ClassCastException ignore) {
            throw new RuntimeException("URL " + url + " must use either http or https");
        }
    }

    @Override
    public void close() throws IOException {
    }
}
