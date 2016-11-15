package com.librato.metrics.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Futures {
    public static <T> T get(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw Throwables.propagate(e);
        } catch (ExecutionException e) {
            throw Throwables.propagate(e);
        }
    }
}
