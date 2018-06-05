package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardHostValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) {
        System.out.println("StandardHostValve invoke");
        Context context = (Context)request.mappingData.context;
        context.getPipeline().getFirst().invoke(request,response);
    }
}
