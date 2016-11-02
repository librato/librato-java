package com.librato.metrics.client;

import java.util.LinkedList;
import java.util.List;

public class Lists {
    public static <T> List<List<T>> partition(List<T> items, int maxSize) {
        List<List<T>> result = new LinkedList<List<T>>();
        List<T> thisList = new LinkedList<T>();
        for (T item : items) {
            thisList.add(item);
            if (thisList.size() >= maxSize) {
                result.add(thisList);
                thisList = new LinkedList<T>();
            }
        }
        if (!thisList.isEmpty()) {
            result.add(thisList);
        }
        return result;
    }
}
