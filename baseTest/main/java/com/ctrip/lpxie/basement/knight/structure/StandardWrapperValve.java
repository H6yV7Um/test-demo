package com.ctrip.lpxie.basement.knight.structure;

import com.ctrip.lpxie.basement.knight.servlet.ApplicationFilterChain;
import com.ctrip.lpxie.basement.knight.servlet.FilterChain;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardWrapperValve extends ValveBase{
    @Override
    public void invoke(Request request, Response response) {
        System.out.println("StandardWrapperValve invoke");
        //这里开始真正执行用户的应用程序逻辑
        //may be through wrapper
        //1,do filter
        //2,do servlet
        StandardWrapper wrapper = (StandardWrapper)request.getMappingData().wrapper;
        FilterChain filterChain = new ApplicationFilterChain(wrapper,wrapper.getServlet());
        filterChain.doFilter(request,response);
    }
}
