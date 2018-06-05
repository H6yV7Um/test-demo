package com.ctrip.lpxie.basement.knight.application;

import com.ctrip.lpxie.basement.knight.servlet.Filter;
import com.ctrip.lpxie.basement.knight.servlet.FilterChain;
import com.ctrip.lpxie.basement.knight.structure.Request;
import com.ctrip.lpxie.basement.knight.structure.Response;

import java.io.PrintWriter;

/**
 * Created by lpxie on 2016/7/23.
 */
public class SelfFilter implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("I am in SelfFilter");
        if(!request.getRequestPath().equals("/hello")){
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            writer.println("HTTP/1.1 302 OK");
            writer.println("Location:/hello");//redirect
            writer.println("Transfer-Encoding:chunked");
            writer.println();
            writer.println("0");
            writer.println();
            writer.flush();
        }else {
            chain.doFilter(request,response);
        }
    }
}
