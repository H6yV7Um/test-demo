package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public interface Pipeline {
    public void setBasic(Valve valve);
    public Valve getBasic();

    public Valve getFirst();

    public void setContainer(Container container);
    public Container getContainer();

    public void addValve(Valve valve);
    public Valve[] getValves();
}
