package com.ctrip.lpxie.basement.interrupt;

import java.util.concurrent.*;

/**
 * Created by lpxie on 2016/12/21.
 */
public class interruptTest {
    public static void main(String[] args){
        ExecutorService service = Executors.newFixedThreadPool(10);
        Future<Object> future = service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                while (true) {
                    Thread.sleep(1000);
                    System.out.println("I am sleeping...");
                }
            }
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            future.cancel(true);
        }
    }
}
