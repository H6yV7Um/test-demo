package com.ctrip.lpxie.basement.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lpxie on 2016/5/18.
 */
public class ClhSpinLock {
    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<Node>();

    public ClhSpinLock(){
        this.node = new ThreadLocal<Node>(){
            protected Node initialValue(){
                return new Node();
            }
        };

        this.prev = new ThreadLocal<Node>(){
            protected Node initialValue(){
                return null;
            }
        };
    }

    public void lock(){
        final Node node = this.node.get();
        node.locked = true;
        Node pred = this.tail.getAndSet(node);
        if(pred == null)//first lock no prev node
            return;

        this.prev.set(pred);
        while (pred.locked){//自旋 等待

        }
    }

    public void unlock(){
        final Node node = this.node.get();
        node.locked = false;
       // this.node.set(this.prev.get());
    }

    private static class Node{
        private volatile boolean locked;
    }

    public static void main(String[] args) {
        final ClhSpinLock lock = new ClhSpinLock();
        lock.lock();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    System.out.println(Thread.currentThread().getId() + " acquired the lock!");
                    lock.unlock();
                }
            }).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("main thread unlock!");
        lock.unlock();
    }
}
