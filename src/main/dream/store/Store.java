package main.dream.store;

import main.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Roman", "Junior Java Job"));
        posts.put(2, new Post(2,"Ramon",  "Middle Java Job"));
        posts.put(3, new Post(3, "Mark", "Senior Java Job"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}