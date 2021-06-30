package main.dream.store;

import main.dream.model.Candidate;
import main.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        /*store.save(new Post(0, "Java Job"));*/
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        for (Candidate can : store.findAllCandidates()) {
            System.out.println(can.getId() + " " + can.getName());
        }
        System.out.println(store.findById(2).getName());

    }
}