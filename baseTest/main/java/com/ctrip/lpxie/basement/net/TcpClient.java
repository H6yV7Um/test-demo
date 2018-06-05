package com.ctrip.lpxie.basement.net;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLEncoder;

/**
 * Created by lpxie on 2016/9/7.
 */
public class TcpClient {
    public static void main(String[] args){

            /*String key = URLEncoder.encode("ddff", "utf-8");
            String response = Request.Get(url + key).connectTimeout(100).socketTimeout(200)
                    .execute().returnContent().asString();
*/

         /*   for(int i = 0;i<1000;i++){
                new Thread(new Runnable() {
                    public void run() {
                        String url = "http://127.0.0.1:8080/riskGraph/searchDN";
                        for(int i = 0;i<100000;i++){
                            String rs = null;
                            try {
                                rs = Request.Post(url).body(new StringEntity("[\n" +
                                        "  \"orderData|1|999\"\n" +
                                        "]")).execute().returnContent().asString();
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                           // System.out.println(rs);
                        }

                    }
                }).start();

            }*/

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 8080), 1 * 1000);//5seconds
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            byte[] bytes = new byte[8096];

            socket.getOutputStream().write("hi i am socket".getBytes());
            Thread.sleep(1000);
            socket.getInputStream().read(bytes);
            System.out.println(new String(bytes));
        }catch (Exception exp){
            //
        }

    }
}
