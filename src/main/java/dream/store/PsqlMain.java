package dream.store;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.findAllUserEmails().forEach(System.out::println);
    }
}