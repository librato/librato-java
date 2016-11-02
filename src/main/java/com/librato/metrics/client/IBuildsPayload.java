package com.librato.metrics.client;

import java.util.List;

interface IBuildsPayload {
    byte[] build(Long epoch, List<IMeasure> batch);
}
