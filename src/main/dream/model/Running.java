package main.dream.model;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Running {
    public static void main(String[] args) {
        FutureTask<String> f = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                System.out.println("!!!");
                return "finish";
            }
        });

        Thread thread1 = new Thread(f);

        Thread thread2 = new Thread(new Runnable() {
            String status;
            @Override
            public void run() {
                while (true) {
                    if (f.isDone()) {
                        try {
                            status = f.get();
                            System.out.println(status);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        thread1.start();
        thread2.start();


    }
}
