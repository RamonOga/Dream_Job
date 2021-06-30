package main.dream.store;

import main.dream.model.Candidate;
import main.dream.model.Post;
import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {

    private static final Store INST = new Store();
    private static AtomicInteger POST_ID = new AtomicInteger(3);
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(3);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<Candidate, List<File>> photos = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Ramon"));
        posts.put(2, new Post(2, "Middle Java Job", "Roman"));
        posts.put(3, new Post(3, "Senior Java Job", "Roman Markelov"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public static Store instOf() {
        return INST;
    }

    public Post findPostById(Integer id) {
        if (!posts.containsKey(id)) {
            throw new NoSuchElementException("Post with " + id + "id not found");
        }
        return posts.get(id);
    }

    public Candidate findCandidateById(Integer id) {
        if (!candidates.containsKey(id)) {
            throw new NoSuchElementException("Post with " + id + "id not found");
        }
        return candidates.get(id);
    }

    public void deleteCandidate(String id) {
        Candidate candidate = candidates.get(Integer.parseInt(id));
        for (File file : photos.get(candidate)) {
            file.delete();
        }
        candidates.remove(Integer.parseInt(id));
    }

    public void deletePost(String id) {
        posts.remove(Integer.parseInt(id));
    }

    public void addCandidatePhoto(Candidate candidate, File file) {
        if (!photos.containsKey(candidate)) {
            List<File> photosList = new CopyOnWriteArrayList<>();
            photosList.add(file);
            photos.put(candidate, photosList);
        } else {
            photos.get(candidate).add(file);
        }
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }


}