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

## Maven Dependency

    <dependency>
        <groupId>com.librato.metrics</groupId>
        <artifactId>librato-java</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    
