package com.librato.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
    private static final String UTF_8 = "UTF-8";
    private static final int defaultConnectTimeoutMillis = 5000;
    private static final int defaultReadTimeoutMillis = 10000;
    private final URL url;
    private final String authHeader;
    private final ExecutorService executor;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    public DefaultHttpPoster(String url, String email, String token) {
        this(url, email, token, defaultConnectTimeoutMillis, defaultReadTimeoutMillis);
    }

    public DefaultHttpPoster(String url, String email, String token, int connectTimeoutMillis, int readTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.authHeader = Authorization.buildAuthHeader(email, token);
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not parse URL", e);
        }
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
        final int responseCode;
        final String responseBody;
        try {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(connectTimeoutMillis);
            connection.setReadTimeout(readTimeoutMillis);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", userAgent);
            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            try {
                outputStream.write(payload.getBytes(Charset.forName(UTF_8)));
            } finally {
                close(outputStream);
            }
            responseCode = connection.getResponseCode();
            InputStream responseStream;
            if (responseCode / 100 == 2) {
                responseStream = connection.getInputStream();
            } else {
                responseStream = connection.getErrorStream();
            }
            responseBody = readResponse(responseStream);
        } finally {
            connection.disconnect();
        }
        return new Response() {
            @Override
            public int getStatusCode() {
                return responseCode;
            }

            @Override
            public String getBody() throws IOException {
                return responseBody;
            }
        };
    }

    private String readResponse(InputStream in) throws IOException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                bos.write(buffer, 0, bytesRead);
            }
            byte[] bytes = bos.toByteArray();
            return new String(bytes, Charset.forName(UTF_8));
        } finally {
            close(in);
        }
    }

    void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            log.warn("Could not close " + closeable, e);
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
