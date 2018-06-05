package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/20.
 */
public class HostConfig implements LifecycleListener {
    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if(event.getType().equals("init")){
            System.out.println("HostConfig init ");
        }else if(event.getType().equals("start")){
            System.out.println("HostConfig start ");
        }
    }
}
