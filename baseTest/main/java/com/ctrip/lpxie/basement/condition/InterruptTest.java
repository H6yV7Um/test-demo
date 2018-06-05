package com.ctrip.lpxie.basement.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lpxie on 2016/5/24.
 */
public class InterruptTest {
    public static void main(String[] args){
        final Lock lock = new ReentrantLock();
        final Condition notFull = lock.newCondition();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        Thread.sleep(30000);
                    }catch (InterruptedException exp){
                        System.out.println("t1有中断标志，在sleep的时候会抛出InterruptedException");
                        break;
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                while (true){
                    /*if((System.currentTimeMillis() - now)%1000 == 0){
                        System.out.println("I am in t2");
                    }*/
                    if(Thread.interrupted()){
                        {
                            System.out.println("t2有中断标志");
                            break;
                        }
                    }
                }
            }
        });

        Thread t3 =  new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("t3获取到锁");
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                    System.out.println("t3在condition的pack里面遇到中断标志InterruptedException");
                }
                lock.unlock();
            }
        });

        t1.start();
        t2.start();
        t3.start();


        try {
            Thread.sleep(1000);
            t1.interrupt();
            Thread.sleep(1000);
            t2.interrupt();
            Thread.sleep(1000);
            //t3.interrupt();
            lock.lock();
            notFull.signal();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
