package com.ctrip.lpxie.basement.knight.servlet;

import com.ctrip.lpxie.basement.knight.structure.Request;
import com.ctrip.lpxie.basement.knight.structure.Response;
import com.ctrip.lpxie.basement.knight.structure.StandardContext;
import com.ctrip.lpxie.basement.knight.structure.Wrapper;

import java.util.HashMap;

/**
 * Created by lpxie on 2016/7/23.
 */
public class ApplicationFilterChain implements FilterChain {
    private Filter[] filters = new Filter[0];

    private Servlet servlet = null;

    private int n = 0;

    private int pos = 0;

    public ApplicationFilterChain(Wrapper wrapper,Servlet servlet){
        //init some parameters
        StandardContext context = (StandardContext)wrapper.getParent();
        HashMap<String, FilterDef> filterDefs = context.getFilterDefs();
        filters = new Filter[filterDefs.keySet().size()];
        int i = 0;
        for(String key : filterDefs.keySet()){
            filters[i++] = filterDefs.get(key).getFilter();
        }
        n = filters.length;
        this.servlet =servlet;
    }

    @Override
    public void doFilter(Request request, Response response) {
        internalDoFilter(request, response);
    }

    private void internalDoFilter(Request request, Response response){
        if(pos < n){
            Filter filter = filters[pos++];
            //this.doFilter(request,response);
            filter.doFilter(request,response,this);
            return;
        }
        //finally do servlet
        servlet.service(request,response);
    }
}
