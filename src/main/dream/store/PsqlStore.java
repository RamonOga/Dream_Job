package main.dream.store;

import main.dream.LogCreator;
import org.apache.commons.dbcp2.BasicDataSource;
import main.dream.model.Candidate;
import main.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
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
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
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
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    private Post create(Post post) {
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
            LOG.error("Message from create method ", e);
        }
        return post;
    }

    private void update(Post post) {
        try(Connection con = pool.getConnection();
        PreparedStatement ps = con.prepareStatement("update post set name = (?) where id = (?)")
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
        } catch (Exception e) {
            LOG.error("Message from update method ", e);
        }
    }

    @Override
    public Post findById(int id) {
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
            LOG.error("Message from findById method ", e);
        }
        return rsl;
    }
}