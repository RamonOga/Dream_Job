package dream.store;

import dream.LogCreator;
import dream.model.*;

import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemStore implements Store {

    static final Logger LOG = LogCreator.getLogger();


    private static AtomicInteger POST_ID = new AtomicInteger(0);
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(0);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<Candidate, List<File>> photos = new ConcurrentHashMap<>();
    private final List<User> users = new CopyOnWriteArrayList<>();
    private final List<City> cites = new CopyOnWriteArrayList<>();
    private static final MemStore INST = new MemStore();


    private MemStore() {
        savePost(new Post(0, "Junior Java Job", "Ramon"));
        savePost(new Post(0, "Middle Java Job", "Roman"));
        savePost(new Post(0, "Senior Java Job", "Roman Markelov"));
        saveCandidate(new Candidate(0, "Junior Java"));
        saveCandidate(new Candidate(0, "Middle Java"));
        saveCandidate(new Candidate(0, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
        photos.put(candidate, new CopyOnWriteArrayList<>());
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }


    @Override
    public User findUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Post findPostById(Integer id) {
        if (!posts.containsKey(id)) {
            String err = "Post with " + id + "id not found";
            LOG.warn(err);
            throw new NoSuchElementException(err);
        }
        return posts.get(id);
    }

    @Override
    public Candidate findCandidateById(Integer id) {
        if (!candidates.containsKey(id)) {
            String err = "Candidate with " + id + "id not found";
            LOG.warn(err);
            throw new NoSuchElementException("Candidate with " + id + "id not found");
        }
        return candidates.get(id);
    }

    @Override
    public void deleteCandidate(String id) {
        Candidate candidate = candidates.get(Integer.parseInt(id));
        List<File> fileList = photos.get(candidate);
        for (File file : fileList ) {
            file.delete();
        }
        candidates.remove(Integer.parseInt(id));
    }

    @Override
    public void deletePost(String id) {
        posts.remove(Integer.parseInt(id));
    }

    @Override
    public void addCandidatePhoto(Candidate candidate, File file) {
        if (!photos.containsKey(candidate)) {
            List<File> photosList = new CopyOnWriteArrayList<>();
            photosList.add(file);
            photos.put(candidate, photosList);
        } else {
            photos.get(candidate).add(file);
        }
    }

    @Override
    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public List<String> findAllUserEmails() {
        List<String> rsl = new CopyOnWriteArrayList<>();
        return users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public List<City> findAllCites() {
        return cites;
    }


    /**
     *
     * ?????? ?? ?????????? ???????????????? ???????????? ???????? ?????????????????????? ???????????????????? ???????????????????? ?? SQL ??????????.
     * ?????????????? ???? ???????? ?????????????????????????? ???????????? ?? MemStore
     */
    @Override
    public void incrementVisitors(Candidate candidate) {

    }

    @Override
    public CandidateVisitors findCandidateVisitorsByCandidateId(Candidate candidate) {
        return null;
    }
}