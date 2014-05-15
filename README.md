# librato-java

Java language bindings for the Librato Metrics API.

## Updating from 0.1.x ?

A bug existed in 0.1.x that stripped out dashes `-` from the metric name before submitting to Librato. This has
been fixed in `1.0.0`.  We made this a major point release because it has the potential to start reporting your
metrics to Librato with a different (but now, correct) metric name.

For example, if you call:

	LibratoBatch batch = ...;
	batch.addGaugeMeasurement("foo.bar-one", 5)
	
The `0.1.x` version of `librato-java` will report this to Librato as `foo.barone`.  If you upgrade to `1.0.0` then
this will report to Librato as `foo.bar-one`.  Note that you will likely need to adjust your instruments, alerts,
etc if you have metrics with dashes in the names and upgrade to this version of the library.

## How to use?

    //
    // Initialize Librato
    //

    String email = "<your Librato e-mail>";
    String apiToken = "<your Librato API token>";
    String apiUrl = "https://metrics-api.librato.com/v1/metrics";

    NingHttpPoster httpPoster = NingHttpPoster.newPoster(email, apiToken, apiUrl);

    int batchSize = <split your counters or gauges to batches by N items;
                     if you are sending several metrics set to count of metrics or great>;
    int agentName = "any string used as part of User-Agent header";

    LibratoBatch libratoBatch = new LibratoBatch(batchSize, Sanitizer.NO_OP, 10, TimeUnit.SECONDS, agentName, httpPoster);

    //
    // Set counters and gauges values
    //

    libratoBatch.addCounterMeasurement("nsa.all-captured-phone-calls", 8703234);

    libratoBatch.addGaugeMeasurement("usa", "nsa.captured-phone-calls.per-minute", 53234);
    libratoBatch.addGaugeMeasurement("germany", "nsa.captured-phone-calls.per-minute", 21367);

    //
    // Send values to Librato server
    //

    String source = "<specify source for all values or leave it (set to null)>";

    long timestamp = System.currentTimeMillis() / 1000;

    // If you are going to aggregate and send metrics in specified interval you can align them by beginning of that interval
    //
    // int monitoringInterval = 60; // seconds
    // timestamp = timestamp - (timestamp % monitoringInterval); // Align by monitoring interval (for any case)

    libratoBatch.post(source, timestamp);

## Maven Dependency

    <dependency>
        <groupId>com.librato.metrics</groupId>
        <artifactId>librato-java</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    
