package main.dream.store;
import main.dream.model.Candidate;
import main.dream.model.Post;
import main.dream.model.User;

import java.io.File;
import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findPostById(Integer id);

    Candidate findCandidateById(Integer id);

    void deleteCandidate(String id);

    void deletePost(String id);

    void addCandidatePhoto(Candidate candidate, File file);

    void addUser(User user);

    User findUserById(int id);



}
