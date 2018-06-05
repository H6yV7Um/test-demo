package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public interface Server extends Lifecycle{

    public void addService(Service service);
    public Service[] findServices();
}
