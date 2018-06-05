package com.ctrip.lpxie.basement.condition;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by lpxie on 2016/5/26.
 */
public class LockSupportTest {
    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I am in pack "+Thread.currentThread().getName());
                LockSupport.park(Thread.currentThread());
                System.out.println("I am in unpack "+Thread.currentThread().getName());
            }
        });
        t1.setName("t1");
        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I am in pack "+Thread.currentThread().getName());
                LockSupport.park(Thread.currentThread());
                System.out.println("I am in unpack "+Thread.currentThread().getName());
            }
        });
        t2.setName("t2");
        final Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I am in pack "+Thread.currentThread().getName());
                LockSupport.park(Thread.currentThread());
                System.out.println("I am in unpack "+Thread.currentThread().getName());
            }
        });
        t3.setName("t3");

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println("t1 "+t1.getState().toString());
                    //System.out.println("t2 "+t2.getState().toString());
                    //System.out.println("t3 "+t3.getState().toString());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t4.setName("t4");
        t4.start();

        t1.start();
        Thread.sleep(1000);
        LockSupport.unpark(t1);
        t2.start();
        Thread.sleep(1000);
        LockSupport.unpark(t2);
        t3.start();
        Thread.sleep(1000);
        LockSupport.unpark(t3);

    }
}
