package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/21.
 */
public class HostLogValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) {
        System.out.println("HostLogValve invoke");
        getNext().invoke(request,response);
        System.out.println("return HostLogValve");
    }
}
