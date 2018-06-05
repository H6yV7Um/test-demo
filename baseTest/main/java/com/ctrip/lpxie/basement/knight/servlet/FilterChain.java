package com.ctrip.lpxie.basement.knight.servlet;

import com.ctrip.lpxie.basement.knight.structure.Request;
import com.ctrip.lpxie.basement.knight.structure.Response;

/**
 * Created by lpxie on 2016/7/23.
 */
public interface FilterChain {
    public void doFilter(Request request,Response response);
}
