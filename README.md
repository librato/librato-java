# librato-java

Java language bindings for the Librato Metrics API.

## Looking for a previous version? 

You can find documentation for versions  < 2.x.x [here](Legacy.md).

## Maven Dependency

    <dependency>
        <groupId>com.librato.metrics</groupId>
        <artifactId>librato-java</artifactId>
        <version>2.0.1</version>
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
        .add(new GaugeMeasure(name, value).setSource("uid:43")
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

