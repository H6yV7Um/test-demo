package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public interface Valve {
    public Valve getNext();
    public void setNext(Valve valve);
    public void invoke(Request request,Response response);
}
