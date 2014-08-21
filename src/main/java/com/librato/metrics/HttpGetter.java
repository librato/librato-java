package com.librato.metrics;

import java.io.IOException;
import java.util.concurrent.Future;

public interface HttpGetter {
	 /**
     * A generic interface to represent an HTTP response
     */
    public static interface Response {
        int getStatusCode();
        String getBody() throws IOException;
    }

   /**
    * GET the payload
    * @param userAgent
    * @param query 
    * @return
    * @throws IOException
    */
    Future<Response> get(String userAgent) throws IOException;

}
