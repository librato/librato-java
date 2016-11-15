package com.librato.metrics.client;

interface IBuildsPayload {
    byte[] build(Measures measures);
}
