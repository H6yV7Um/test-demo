package com.ctrip.lpxie.basement.knight;

import com.ctrip.lpxie.basement.knight.structure.DefaultAdapter;

import java.net.Socket;

/**
 * async execute this connection
 * Created by lpxie on 2016/7/18.
 * fixme problem:
 * 1.how to set keep-alive to let browser not close the connection
 */
public class SocketProcessor implements Runnable {
    private Socket socket;

    private DefaultAdapter adapter;
    private boolean launch = false;

    public SocketProcessor(Socket socket,DefaultAdapter adapter){
        this.socket = socket;
        this.adapter = adapter;
    }

    @Override
    public void run() {
        try {
            launch = isLaunch(socket);
            if(!launch)
                socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
            launch = false;
        }finally {
            if(launch){
                ConnectionServer.getServerExecutor().execute(new SocketProcessor(socket,adapter));
            }
        }
        socket = null;
    }

    private boolean isLaunch(Socket socket) throws Exception{
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(10000);
            SocketWrapper socketWrapper = new SocketWrapper(socket.getInputStream(),socket.getOutputStream());

            adapter.service(socketWrapper.getRequest(),socketWrapper.getResponse());

            return true;
        }catch (Exception exp)
        {
            exp.printStackTrace();
            socket.close();
            return false;
        }
    }
}
