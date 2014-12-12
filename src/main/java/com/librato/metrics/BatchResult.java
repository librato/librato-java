package com.librato.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Composes the results of processing a batch.  A batch may be broken up into multiple
 * postings.
 */
public class BatchResult {
    private final List<PostResult> posts = new ArrayList<PostResult>();

    public List<PostResult> getPosts() {
        return posts;
    }

    void addPostResult(PostResult result) {
        posts.add(result);
    }

    public List<PostResult> getFailedPosts() {
        List<PostResult> failed = new ArrayList<PostResult>();
        for (PostResult post : posts) {
            if (!post.success()) {
                failed.add(post);
            }
        }
        return failed;
    }

    public boolean success() {
        for (PostResult post : posts) {
            if (!post.success()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "BatchResult{" +
                "posts=" + posts +
                '}';
    }
}
