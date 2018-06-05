package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardContextValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) {
        System.out.println("StandardContextValve invoke");
        Wrapper wrapper = (StandardWrapper)request.mappingData.wrapper;
        wrapper.getPipeline().getFirst().invoke(request,response);
    }
}
