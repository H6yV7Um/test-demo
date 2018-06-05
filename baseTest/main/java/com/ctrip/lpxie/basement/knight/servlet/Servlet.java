package com.ctrip.lpxie.basement.knight.servlet;

import com.ctrip.lpxie.basement.knight.structure.Request;
import com.ctrip.lpxie.basement.knight.structure.Response;

/**
 * Created by lpxie on 2016/7/22.
 */
public interface Servlet {
    public void init();
    public void service(Request request,Response response);
    public void destroy();
}
