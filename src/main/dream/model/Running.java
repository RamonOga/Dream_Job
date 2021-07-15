package main.dream.model;

import java.util.concurrent.atomic.AtomicInteger;

class MemStore {

  /*  private static AtomicInteger POST_ID = new AtomicInteger(0);
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(0);*/
    private static MemStore INST = new MemStore();

    private MemStore() {
       /* POST_ID.incrementAndGet();
        CANDIDATE_ID.incrementAndGet();*/
    }

    public static MemStore instOf() {
        return INST;
    }
}

public class Running {
    public static void main(String[] args) {
        MemStore m = MemStore.instOf();
    }
}
