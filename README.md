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
        <version>1.0.1</version>
    </dependency>
    
## Usage

To use `librato-java` you must first setup the `NingHttpPoster` (or use your own implementation), which can then be
re-used across subsequent `LibratoBatch` submissions.

### Setup the HTTP Poster

    static String email = `your Librato email`
    static String apiToken = `your Librato api token`
    static String apiUrl = "https://metrics-api.librato.com/v1/metrics"
    static HttpPoster poster = NingHttpPoster.newPoster(email, apiToken, apiUrl)
    
### Using LibratoBatch
    
The `LibratoBatch` composes all of the metrics in a submission to Librato. One first creates a new `LibratoBatch`,
and then adds gauges and counters to it, and then finally posts the batch to Librato.

	static int batchSize = 300
	static long timeout = 10L
	static TimeUnit timeoutUnit = TimeUnit.SECONDS
	static String agent = `your http agent name -- will be saved with your metric`
    LibratoBatch batch = new LibratoBatch(batchSize, sanitizer, timeout, timeoutUnit, agent, poster)
    
Once you have created your `batch`, then you can add measurements to it.

    batch.addGaugeMeasurement("apples", 100)
    batch.addCounterMeasurement("bytes-in", 42)
    
You can also specify sources per-gauge/counter as of version `1.0.1`:
 
    batch.addGaugeMeasurement("east-orchard", "apples", 100)
    batch.addGaugeMeasurement("west-orchard", "apples", 200)
    
Now, it's time to submit to Librato. One must specify an epoch that will be applied to all of the measurements
as well as a source that will be applied to measurements that did not have a source specified when adding to
the `batch`.

    long epoch = System.currentTimeMillis / 1000
    String source = `typically machine name, or leave as null`
    batch.post(source, epoch)
    

   
