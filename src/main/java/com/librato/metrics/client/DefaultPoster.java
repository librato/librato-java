package com.librato.metrics.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class DefaultPoster implements IPoster {
    private static final Logger log = LoggerFactory.getLogger(DefaultPoster.class);

    @Override
    public HttpResponse post(URI uri, Duration connectTimeout, Duration readTimeout, Map<String, String> headers, byte[] payload) {
        try {
            HttpURLConnection connection = open(uri);
            final int responseCode;
            final byte[] responseBody;
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout((int) connectTimeout.to(MILLISECONDS));
            connection.setReadTimeout((int) readTimeout.to(MILLISECONDS));
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            for (String header : headers.keySet()) {
                connection.setRequestProperty(header, headers.get(header));
            }
            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            try {
                outputStream.write(payload);
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
            return new HttpResponse() {
                @Override
                public int getResponseCode() {
                    return responseCode;
                }

                @Override
                public byte[] getResponseBody() {
                    return responseBody;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    HttpURLConnection open(URI url) throws IOException {
        try {
            return (HttpURLConnection) url.toURL().openConnection();
        } catch (ClassCastException ignore) {
            throw new RuntimeException("URL " + url + " must use either http or https");
        }
    }

    private byte[] readResponse(InputStream in) throws IOException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
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

}
