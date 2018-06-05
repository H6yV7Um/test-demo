package com.ctrip.lpxie.basement.knight.structure;

import java.util.EventObject;

/**
 * Created by lpxie on 2016/7/20.
 */
public final class LifecycleEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private Object data = null;

    private String type = null;

    public LifecycleEvent(Lifecycle lifecycle,String type,Object data) {
        super(lifecycle);
        this.data = data;
        this.type = type;
    }

    public Object getData(){
        return this.data;
    }

    public Lifecycle getLifecycle(){
        return (Lifecycle)getSource();
    }

    public String getType(){
        return this.type;
    }
}
