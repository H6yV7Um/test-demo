package com.ctrip.lpxie.basement.javaBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by lpxie on 2016/12/19.
 */
public class Main {
    public static void main(String[] args){
        int num = 4;
        CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
            @Override
            public void run() {
                System.out.println("Current thread "+Thread.currentThread().getName()+" is chosen to do something...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int i = 0;i<num;i++){
            new Thread(new Writer(barrier)).start();
        }
    }

    static class Writer implements Runnable{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("thread = " + Thread.currentThread().getName() + " is writing...");
            try {
                Thread.sleep(3000);
                System.out.println("线程" + Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" write completely,continue to do others...");
        }
    }
}
