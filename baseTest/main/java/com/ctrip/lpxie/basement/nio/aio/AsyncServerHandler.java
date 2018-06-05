package com.ctrip.lpxie.basement.nio.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lpxie on 2016/8/1.
 */
public class AsyncServerHandler implements Runnable {
    public CountDownLatch latch;
    public AsynchronousServerSocketChannel channel;
    public AsyncServerHandler(int port) throws Exception{
        channel = AsynchronousServerSocketChannel.open();
        channel.bind(new InetSocketAddress(port));
        System.out.println("server starting...");
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        channel.accept(this,new AcceptHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
