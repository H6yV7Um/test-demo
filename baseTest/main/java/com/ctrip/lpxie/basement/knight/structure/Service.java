package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public interface Service extends Lifecycle{
    public void setServer(Server server);
    public Container getContainer();
    public void setContainer(Container container);
}
