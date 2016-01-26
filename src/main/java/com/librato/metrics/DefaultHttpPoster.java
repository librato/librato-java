package com.librato.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(DefaultHttpPoster.class);
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
        //this.executor = new ThreadPoolExecutor(1, 1, 120, TimeUnit.SECONDS, queue, threadFactory, handler);
        this.executor = Executors.newSingleThreadExecutor();
    }

    class CouldNotPostMeasurementsException extends RuntimeException {
        public CouldNotPostMeasurementsException(Throwable cause) {
            super("Could not post measures to " + url, cause);
        }
    }

    @Override
    public Future<Response> post(final String userAgent, final String payload) throws IOException {
        return executor.submit(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                try {
                    return doPost(userAgent, payload);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CouldNotPostMeasurementsException(e);
                }
            }
        });
    }

    Response doPost(String userAgent, final String payload) throws IOException {
        HttpURLConnection connection = open(url);
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
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

    HttpURLConnection open(URL url) throws IOException {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (ClassCastException ignore) {
            throw new RuntimeException("URL " + url + " must use either http or https");
        }
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
        try {
            long timeout = 1L;
            TimeUnit unit = TimeUnit.MINUTES;
            if (!executor.awaitTermination(timeout, unit)) {
                log.warn("Could not shutdown after {} {}", timeout, unit);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted during shutdown");
        }
    }
}
