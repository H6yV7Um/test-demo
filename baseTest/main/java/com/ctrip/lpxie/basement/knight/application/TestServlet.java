package com.ctrip.lpxie.basement.knight.application;

import com.ctrip.lpxie.basement.knight.servlet.Servlet;
import com.ctrip.lpxie.basement.knight.structure.Request;
import com.ctrip.lpxie.basement.knight.structure.Response;

import java.io.PrintWriter;

/**
 * Created by lpxie on 2016/7/24.
 */
public class TestServlet implements Servlet {
    @Override
    public void init() {

    }

    @Override
    public void service(Request request, Response response) {
        if(!request.getRequestPath().equals("test"))
            return;
        System.out.println("I am in SelfServlet");
        System.out.println(request.getRequestPath());
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        writer.println("HTTP/1.1 200");
        writer.println("Transfer-Encoding:chunked");
        writer.println("Connection:keep-alive");
        writer.println("Content-Type:application/json;charset=UTF-8");
        writer.println();

        writer.println("3");
        writer.println("{\r\n");
        for(Object key : request.getHeaders().keySet()){
            String line = key + ":" + request.getHeaders().get(key)+"\r\n";
            writer.println(Integer.toHexString(line.length()));
            writer.println(line);
        }

        writer.println("3");
        writer.println("}\r\n");


        writer.println("0");
        writer.println();

        writer.flush();
    }

    @Override
    public void destroy() {

    }
}
