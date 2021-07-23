package main.dream.store;

import main.dream.model.Candidate;
import main.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.findAllUserEmails().forEach(System.out::println);
    }
}