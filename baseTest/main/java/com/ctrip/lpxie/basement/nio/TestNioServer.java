package com.ctrip.lpxie.basement.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by lpxie on 2016/7/31.
 */
public class TestNioServer {
    public static void main(String[] args){
        try {
            selector();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void selector() throws IOException{
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(8080),500);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server Start----8080:");
        SocketChannel client = null;
        ByteBuffer send = ByteBuffer.allocate(1024);
        send.put("data come from server".getBytes());
        while (true){
            selector.select();
            Set selectedKeys = selector.selectedKeys();
            Iterator it = selectedKeys.iterator();
            while (it.hasNext()){
                SelectionKey key = (SelectionKey)it.next();
                if((key.readyOps() &  SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
                    ServerSocketChannel sscChannel = (ServerSocketChannel)key.channel();
                    client = sscChannel.accept();//block?
                    if(client == null)
                        continue;
                    System.out.println("new connect from:"+client.getRemoteAddress());
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
                else if(key.isReadable()){
                    try {
                        buffer.clear();
                        client = (SocketChannel) key.channel();
                        int n = client.read(buffer);
                        if (n > 0) {
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);
                            System.out.println(client.getRemoteAddress()+" accept message :" + new String(bytes, "utf-8"));
                        } else if (n < 0) {//close this socket channel
                            key.cancel();
                            client.close();
                        }
                        key.interestOps(SelectionKey.OP_WRITE);
                    }catch (IOException exp){
                        key.cancel();
                        client.close();
                       exp.printStackTrace();
                    }catch (CancelledKeyException exp){
                        key.cancel();
                        client.close();
                        exp.printStackTrace();
                    }
                }else if(key.isWritable()){
                    try {
                    // 将缓冲区清空以备下次写入
                    // 返回为之创建此键的通道。
                    client = (SocketChannel) key.channel();
                    // 输出到通道
                    client.write(ByteBuffer.wrap("data come from server".getBytes()));
                    key.interestOps(SelectionKey.OP_READ);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    }catch (IOException exp){
                        key.cancel();
                        client.close();
                        exp.printStackTrace();
                    }catch (CancelledKeyException exp){
                        key.cancel();
                        client.close();
                        exp.printStackTrace();
                    }
                }
                it.remove();
            }
            //Thread.sleep();
        }
    }
}
