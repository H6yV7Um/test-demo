package com.ctrip.lpxie.basement.nio.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by lpxie on 2016/8/1.
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel channel, AsyncServerHandler serverHandler) {
        TestAioServer.clientCount++;
        System.out.println("connections:" + TestAioServer.clientCount);
        serverHandler.channel.accept(serverHandler, this);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer,buffer,new ReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AsyncServerHandler attachment) {

    }
}
