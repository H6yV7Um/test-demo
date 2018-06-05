package com.ctrip.lpxie.basement.servlet;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by lpxie on 2016/5/6.
 */
public class HelloFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("HelloFilter doFilter");

        chain.doFilter(request,response);//if this is the end ,then go to servlet
    }

    public void destroy() {

    }
}
