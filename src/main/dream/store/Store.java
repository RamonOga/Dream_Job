package main.dream.store;
import main.dream.model.Candidate;
import main.dream.model.Post;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findPostById(Integer id);

    Candidate findCandidateById(Integer id);

    public void deleteCandidate(String id);

    public void deletePost(String id);
}
