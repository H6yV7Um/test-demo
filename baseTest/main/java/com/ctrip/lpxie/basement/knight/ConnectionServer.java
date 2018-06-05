package com.ctrip.lpxie.basement.knight;

import com.ctrip.lpxie.basement.knight.structure.DefaultAdapter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * this class used to doProcess client requests by accept bytes array
 * use nio mode
 * Created by lpxie on 2016/7/18.
 */
public class ConnectionServer {

    DefaultAdapter adapter = new DefaultAdapter();

    private ServerSocket serverSocket;
    public int port = 8080;//change to read config files

    private boolean launch = false;

    private static Executor serverExecutor = new ThreadPoolExecutor(16,64,60, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());//no bounded queue

    public static Executor getServerExecutor() {
        return serverExecutor;
    }

    public void startInternal(){
        try {
            serverSocket = new ServerSocket(port);
            launch = true;
            startUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //wait connections come in
    private void startUp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (launch){
                    try {
                        Socket socket = serverSocket.accept();//every time one request fixme multiple parallel requests
                        socket.setKeepAlive(true);
                        //executor pool do these jobs
                        SocketProcessor socketProcessor = new SocketProcessor(socket,adapter);
                        serverExecutor.execute(socketProcessor);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public DefaultAdapter getAdapter() {
        return adapter;
    }
}

