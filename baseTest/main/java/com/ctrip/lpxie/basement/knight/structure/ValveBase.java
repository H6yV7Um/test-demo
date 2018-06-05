package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public abstract class ValveBase implements Valve{
    private Valve next = null;

    @Override
    public Valve getNext() {
        return next;
    }

    public void setNext(Valve next) {
        this.next = next;
    }
}
