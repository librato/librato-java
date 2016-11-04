package com.librato.metrics.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tags {
    public static Map<String, String> toMap(List<Tag> tags) {
        if (tags.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new HashMap<String, String>();
        for (Tag tag : tags) {
            String name = tag.name;
            String value = tag.value;
            map.put(name, value);
        }
        return map;
    }
}
