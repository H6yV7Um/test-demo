package com.ctrip.lpxie.basement.knight.application;

import com.ctrip.lpxie.basement.knight.servlet.Filter;
import com.ctrip.lpxie.basement.knight.servlet.FilterChain;
import com.ctrip.lpxie.basement.knight.structure.Request;
import com.ctrip.lpxie.basement.knight.structure.Response;

/**
 * Created by lpxie on 2016/7/24.
 */
public class TestFilter implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        chain.doFilter(request,response);
    }
}
