# librato-java

Java language bindings for the Librato Metrics API.

## Looking for a previous version? 

You can find documentation for versions  < 2.x.x [here](Legacy.md).

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
        <version>2.1.0</version>
    </dependency>

## Setup

You must first initialize the client:

    LibratoClient client = LibratoClient.builder(email, apiToken)
        // these are optional
        .setConnectTimeout(new Duration(5, SECONDS))
        .setReadTimeout(new Duration(5, SECONDS))
        .setAgentIdentifier("my app name")
        // and finally build
        .build();
    
Once your client has been built, you can submit measures to the Librato
API:

    PostMeasuresResult result = client.postMeasures(new Measures()
        .add(new GaugeMeasure(name, value))
        .add(new GaugeMeasure(name, value).setSource("uid:43"))
        .add(new GaugeMeasure(name, sum, count, min, max))
        .add(new GaugeMeasure(name, sum, count, min, max, sumSquares))
        .add(new CounterMeasure(name, value))
        .add(new TaggedMeasure(name, value, tag, tag))
        .add(new TaggedMeasure(name, sum, count, min, max, tag, tag ,tag)));
    
    for (PostResult postResult : result.results) {
        if (result.isError()) {
            log.error(result.toString);
        }
    }

Note that if you wish to submit tagged measures you need to first contact
support@librato.com to get early access to this new feature.

