package com.ctrip.lpxie.basement.nio;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Created by lpxie on 2016/7/31.
 */
public class TestNioClient {
    public static void main(String[] args) throws IOException {

        for(int j = 0;j<100;j++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    SocketChannel sc = SocketChannel.open();
                    final Selector selector = Selector.open();
                    sc.configureBlocking(false);
                    sc.connect(new InetSocketAddress("localhost", 8080));
                    sc.register(selector, SelectionKey.OP_CONNECT);

                    final ByteBuffer receive = ByteBuffer.allocate(1024);


                        int i = 0;
                        while (true) {
                            // Ñ¡Ôñ
                            if (selector.select() == 0) {
                                continue;
                            }
                            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                            while (it.hasNext()) {
                                SelectionKey key = it.next();
                                SocketChannel sc1 = (SocketChannel) key.channel();
                                if (key.isConnectable()) {
                                    if (sc1.isConnectionPending()) {
                                        sc1.finishConnect();
                                        System.out.println(sc1.getLocalAddress()+" connect completely");
                                    }
                                    sc1.write(ByteBuffer.wrap("data com from client".getBytes()));
                                    key.interestOps(SelectionKey.OP_READ);
                                } else {
                                    if (key.isReadable()) {
                                        receive.clear();
                                        sc1.read(receive);
                                        receive.flip();
                                        byte[] bytes = new byte[receive.remaining()];
                                        receive.get(bytes);
                                        System.out.println(Thread.currentThread().getName()+":"+new String(bytes, "utf-8"));
                                        if (i >= 10) {
                                            key.cancel();
                                            sc1.close();
                                        }
                                        key.interestOps(SelectionKey.OP_WRITE);
                                    } else if (key.isWritable()) {
                                        sc1.write(ByteBuffer.wrap("data com from client FFF".getBytes()));
                                        i++;
                                        key.interestOps(SelectionKey.OP_READ);
                                    }
                                }
                                it.remove();
                            }
                        }
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
