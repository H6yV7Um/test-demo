package com.ctrip.lpxie.basement.nio.aio;

/**
 * Created by lpxie on 2016/8/1.
 */
public class TestAioServer {
    private static int DEFAULT_PORT = 12345;
    private static AsyncServerHandler serverHandler;
    public volatile static long clientCount = 0;
    public static void start(){
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port){
        if(serverHandler != null)
        {
            return;
        }
        try {
            serverHandler  = new AsyncServerHandler(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
