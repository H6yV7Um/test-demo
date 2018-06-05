package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/20.
 */
public interface Lifecycle {
    public void init();
    public void start();
    public void stop();
    public void destroy();
    public void addLifecycleListener(LifecycleListener lifecycleListener);
    public void removeLifecycleListener(LifecycleListener lifecycleListener);
    public LifecycleListener[] findLifecycleListeners();
}
