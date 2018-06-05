package com.ctrip.lpxie.basement.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by lpxie on 2016/5/6.
 */
public class NewWorld extends HttpServlet {
    private String message;

    public void init() throws ServletException {
        message = "New World";
        System.out.println("NewWorld invoke init method");
    }

    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        System.out.println("invoke service method");
        doGet((HttpServletRequest)req,(HttpServletResponse)res);
    }

    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        System.out.println("invoke destroy method");
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<h1>"+message+"</h1>");
    }

    public void destroy(){
        System.out.println("invoke destroy method");
    }
}
