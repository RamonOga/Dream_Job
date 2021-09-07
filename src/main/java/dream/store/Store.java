package dream.store;
import dream.exceptions.AlreadyEmailException;
import dream.model.*;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 *
 * Данный интерфейс желательно разделить на несколько. Но цель задания была в другом.
 */

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

    void incrementVisitors(Candidate candidate);

    CandidateVisitors findCandidateVisitorsByCandidateId(Candidate candidate);

}
