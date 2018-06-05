package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public interface Engine extends Container{
    public Service getService();
    public void setService(Service service);
}
