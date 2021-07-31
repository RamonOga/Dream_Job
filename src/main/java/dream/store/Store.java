package dream.store;
import dream.exceptions.AlreadyEmailException;
import dream.model.Candidate;
import dream.model.City;
import dream.model.Post;
import dream.model.User;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface Store {

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    List<String> findAllUserEmails();

    List<City> findAllCites();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findPostById(Integer id);

    Candidate findCandidateById(Integer id);

    void deleteCandidate(String id);

    void deletePost(String id);

    void addCandidatePhoto(Candidate candidate, File file);

    void addUser(User user) throws AlreadyEmailException;

    User findUserById(int id);

    User findUserByEmail(String email);

}
