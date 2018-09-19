package com.librato.metrics.client;

import com.fasterxml.jackson.annotation.JsonTypeName;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@JsonTypeName("okhttp")
public class OkHttpPoster implements IPoster {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public final OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    public HttpResponse post(String uri, Duration connectTimeout, Duration readTimeout, Map<String, String> headers, byte[] payload) {
        OkHttpClient myClient = client.newBuilder()
                .connectTimeout(connectTimeout.to(MILLISECONDS), MILLISECONDS)
                .readTimeout(readTimeout.to(MILLISECONDS), MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(JSON, payload);
        Request.Builder builder = new Request.Builder()
                .url(uri)
                .post(body);
        for (Map.Entry<String, String> header: headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }

        try {
            Response response = myClient.newCall(builder.build()).execute();
            return OkHttpResponse.fromResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class OkHttpResponse implements HttpResponse {
        final int responseCode;
        final byte[] responseBody;

        private OkHttpResponse(int responseCode, byte[] responseBody) {
            this.responseCode = responseCode;
            this.responseBody = responseBody;
        }
        static OkHttpResponse fromResponse(Response response) throws IOException {
            ResponseBody body = response.body();
            if (body != null) {
                return new OkHttpResponse(response.code(), body.bytes());
            } else {
                return new OkHttpResponse(response.code(), new byte[0]);
            }
        }

        @Override
        public int getResponseCode() {
            return responseCode;
        }

        @Override
        public byte[] getResponseBody() {
            return responseBody;
        }
    }
}
