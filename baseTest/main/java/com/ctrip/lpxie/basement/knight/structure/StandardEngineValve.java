package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardEngineValve extends ValveBase {

    @Override
    public void invoke(Request request, Response response) {
        System.out.println("StandardEngineValve invoke");
        Host host = (Host)request.mappingData.host;
        host.getPipeline().getFirst().invoke(request,response);
    }
}
