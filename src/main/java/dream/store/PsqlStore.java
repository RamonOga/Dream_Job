package dream.store;

import dream.LogCreator;
import dream.exceptions.AlreadyEmailException;
import dream.model.*;
import org.apache.commons.dbcp2.BasicDataSource;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import org.apache.logging.log4j.Logger;


public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();
    static private final Logger LOG = LogCreator.getLogger();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    private Post createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Message from createPost method ", e);
        }
        return post;
    }

    private Candidate createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Message from createCandidate method ", e);
        }
        return candidate;
    }

    private void updatePost(Post post) {
        try(Connection con = pool.getConnection();
            PreparedStatement ps = con.prepareStatement("update post set name = (?) where id = (?)")
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Message from updatePost method ", e);
        }
    }

    private void updateCandidate(Candidate candidate) {
        try(Connection con = pool.getConnection();
            PreparedStatement ps = con.prepareStatement("update candidate set name = (?) where id = (?)")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Message from updateCandidate method ", e);
        }
    }

    private CandidateVisitors addCandidateVisitors(Candidate candidate) {
        CandidateVisitors visitors = new CandidateVisitors(0, candidate, 0);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate_visitors(count, candidate_id) VALUES ((?), (?))",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, visitors.getCount());
            ps.setInt(2, candidate.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    visitors.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Message from  addCandidateVisitors method ", e);
        }
        if (visitors.getId() == 0) {
            throw new NoSuchElementException("something went wrong in addCandidateVisitors()");
        }
        return visitors;
    }


    @Override
    public void addUser(User user) throws AlreadyEmailException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO users(name, email, password) VALUES ((?), (?), (?))",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Message from addUser method ", e);
            throw new AlreadyEmailException();
        }
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
            addCandidateVisitors(candidate);
        } else if (findCandidateById(candidate.getId()) != null) {
            updateCandidate(candidate);
        }
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else if(findPostById(post.getId()) != null) {
            updatePost(post);
        }
    }

    @Override
    public void deleteCandidate(String id) {
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("delete from candidate cascade where id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, Integer.parseInt(id));
            ps.execute();

        } catch (Exception e) {
            LOG.error("Message from deleteCandidate method ", e);
        }
    }

    @Override
    public void deletePost(String id) {
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("delete from post where id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, Integer.parseInt(id));
            ps.execute();

        } catch (Exception e) {
            LOG.error("Message from deletePost method ", e);
        }
    }

    @Override
    public User findUserById(int id) {
        User rsl = null;
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("select * from users where id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    rsl = new User(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                            );
                }
            }

        } catch (Exception e) {
            LOG.error("Message from findUserById method ", e);
        }
        return rsl;
    }

    @Override
    public User findUserByEmail(String email) {
        User rsl = null;
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("select * from users where email = (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, email);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    rsl = new User(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }

        } catch (Exception e) {
            LOG.error("Message from findUserByEmail method ", e);
        }
        return rsl;
    }

    @Override
    public Post findPostById(Integer id) {
        Post rsl = null;
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("select * from post where id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    rsl = new Post(rs.getInt("id"), rs.getString("name"));
                }
            }

        } catch (Exception e) {
            LOG.error("Message from findPostById method ", e);
        }
        return rsl;
    }

    @Override
    public Candidate findCandidateById(Integer id) {
        Candidate rsl = null;
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("select * from candidate where id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    rsl = new Candidate(rs.getInt("id"), rs.getString("name"));
                }
            }

        } catch (Exception e) {
            LOG.error("Message from findCandidateById method ", e);
        }
        return rsl;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post order by id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Message from findAllPosts method ", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> rsl = new ArrayList<>();
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate order by id")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rsl.add(new Candidate(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Message from findAllCandidates method ", e);
        }
        return rsl;
    }

    @Override
    public List<String> findAllUserEmails() {
        List<String> rsl = new ArrayList<>();
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM users order by id")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rsl.add(rs.getString("email"));
                }
            }
        } catch (Exception e) {
            LOG.error("Message from findAllCandidates method ", e);
        }
        return rsl;
    }

    @Override
    public List<City> findAllCites() {
        List<City> rsl = new ArrayList<>();
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM city order by name")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rsl.add(new City(rs.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Message from findAllCites method ", e);
        }
        return rsl;
    }

    @Override
    public void addCandidatePhoto(Candidate candidate, File file) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO photo(path, candidate_id) VALUES ((?), (?))"
                                                            , PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, file.getPath());
            ps.setInt(2, candidate.getId());
            ps.execute();

        } catch (Exception e) {
            LOG.error("Message from createCandidatePhoto method ", e);
        }
    }

    @Override
    public void incrementVisitors(Candidate candidate) {
        try(Connection con = pool.getConnection();
            PreparedStatement ps = con.prepareStatement("update candidate_visitors" +
                    " set count = count + 1 " +
                    "where candidate_id = (?)")
        ) {
            ps.setInt(1, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Message from incrementVisitors method ", e);
        }
    }

    @Override
    public CandidateVisitors findCandidateVisitorsByCandidateId(Candidate candidate) {
        CandidateVisitors visitors = null;
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("select * from candidate_visitors " +
                            "where candidate_id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, candidate.getId());
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    visitors = new CandidateVisitors(rs.getInt("id"),
                            candidate,
                            rs.getInt("count"));
                }
            }

        } catch (Exception e) {
            LOG.error("Message from findCandidateVisitorsByCandidateId method ", e);
        }

        if (visitors == null) {
            throw new NoSuchElementException("something went wrong in findCandidateVisitorsByCandidateId()");
        }
        return visitors;
    }
}