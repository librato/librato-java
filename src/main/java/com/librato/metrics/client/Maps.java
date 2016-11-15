package com.librato.metrics.client;

import java.util.Collection;
import java.util.Map;

public class Maps {
    public static void putIfNotNull(Map<String, Object> map, String key, Object object) {
        if (object != null) {
            map.put(key, object);
        }
    }

    public static void putIfNotEmpty(Map<String, Object> map, String key, Collection objects) {
        if (objects != null && !objects.isEmpty()) {
            map.put(key, objects);
        }
    }

    public static void putIfNotEmpty(Map<String, Object> map, String key, Map objects) {
        if (objects != null && !objects.isEmpty()) {
            map.put(key, objects);
        }
    }

}
