package com.ctrip.lpxie.basement.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by lpxie on 2016/5/18.
 */
public class LockPack {
    public static void main(String[] args){
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                LockSupport.park();
                System.out.println("thread " + Thread.currentThread().getId() + " awake!");
                System.out.println("thread " + Thread.currentThread().isInterrupted());
            }
        });

        t.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
    }
}
