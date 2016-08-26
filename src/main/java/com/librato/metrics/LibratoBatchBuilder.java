package com.librato.metrics;

import java.util.concurrent.TimeUnit;

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
public class LibratoBatchBuilder {

	public static final String LIBRATO_METRICS_URL_V1 = "https://metrics-api.librato.com/v1/metrics";
	public static final String DEFAULT_USER_AGENT = "Librato Java Library";
	public static final int DEFAULT_POST_BATCH_SIZE = 300;
	public static final long DEFAULT_TIMEOUT_SECONDS = 10L;
	final private String email;
	final private String apiKey;
	private Long seconds = DEFAULT_TIMEOUT_SECONDS;
	private int batchSize = DEFAULT_POST_BATCH_SIZE;
	private Sanitizer sanitizer = Sanitizer.NO_OP;

	public LibratoBatchBuilder(String email, String apiKey) {
		this.email = email;
		this.apiKey = apiKey;
	}

	public LibratoBatchBuilder withTimeoutInSeconds(Long seconds) {
		this.seconds = seconds;
		return this;
	}

	public LibratoBatchBuilder withBatchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}

	public LibratoBatchBuilder withSanitizer(Sanitizer sanitizer) {
		this.sanitizer = sanitizer;
		return this;
	}

	public LibratoBatch build() {
		final DefaultHttpPoster httpPoster = new DefaultHttpPoster(LIBRATO_METRICS_URL_V1, email, apiKey);
		return new LibratoBatch(batchSize, sanitizer, seconds, TimeUnit.SECONDS, DEFAULT_USER_AGENT, httpPoster);
	}
}
