package com.ctrip.lpxie.basement.schedule;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lpxie on 2017/2/22.
 */
public class ScheduleAnalysis {
    public static void main(String[] args){
        int coreSize = 4;
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(coreSize, new ThreadFactory() {
            private ThreadGroup group;
            String namePrefix = "schedule";
            AtomicLong threadNumber = new AtomicLong(1);
            @Override
            public Thread newThread(Runnable r) {
                group = new ThreadGroup("scheduleGroup");
                Thread t = new Thread(group, r,
                        namePrefix + threadNumber.getAndIncrement(),
                        0);
                return t;
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                throw new RejectedExecutionException("Task " + r.toString() +
                        " rejected from " +
                        executor.toString());
            }
        });

        /*for(int i=0;i<coreSize;i++)
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true){
                    if(count>=5)
                        break;
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+"\tCurrent count:"+count);
                }
            }
        });*/

        final long start = System.currentTimeMillis();
        executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("delayed=" + ((System.currentTimeMillis() - start) / 1000));
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
