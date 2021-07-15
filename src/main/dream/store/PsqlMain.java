package main.dream.store;

import main.dream.model.Candidate;
import main.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.findCandidateById(99);

        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        for (Candidate can : store.findAllCandidates()) {
            System.out.println(can.getId() + " " + can.getName());
        }
        System.out.println(store.findCandidateById(2).getName());

    }
}