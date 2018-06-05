package com.ctrip.lpxie.basement.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lpxie on 2016/5/23.
 */
public class ConditionTest {
    final static Lock lock = new ReentrantLock();
    final static Condition notFull = lock.newCondition();
    final static Condition notEmpty = lock.newCondition();

    public static void main(String[] args){
        //for(int i=0;i<2;i++)
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("拿到锁:"+lock);
                try{
                    System.out.println("等待一个信号："+lock);
                    System.out.println("释放锁:"+lock);
                    notFull.await();
                    System.out.println("拿到锁:"+lock);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.println("释放锁:"+lock);
                }
            }
        }).start();

       //for(int i=0;i<20;i++)
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("拿到锁:"+lock);
                try{
                    System.out.println("发送一个信号："+lock);
                    notEmpty.signal();
                } finally {
                    lock.unlock();
                    System.out.println("释放锁:"+lock);
                }
            }
        }).start();
    }
}
