package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public interface Container extends Lifecycle{
    public Pipeline getPipeline();
    public Container getParent();
    public void setParent(Container container);
    public String getName();
    public Container[] findChildren();

    public void fireContainerEvent(String type, Object data);

    public void addContainerListener(ContainerListener listener);

}
