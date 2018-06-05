package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/20.
 */
public abstract class LifecycleBase implements Lifecycle {

    /**
     * Used to handle firing lifecycle events.
     * TODO: Consider merging LifecycleSupport into this class.
     */
    private LifecycleSupport lifecycle = new LifecycleSupport(this);

    @Override
    public void init() {
        //do something self
        //do sub-class init
        setStateInternal("init",null);

        initInternal();
    }

    protected abstract void initInternal();

    @Override
    public void start() {
        setStateInternal("start",null);

        startInternal();
    }

    protected abstract void startInternal();

    @Override
    public void stop() {
        stopInternal();
    }

    protected abstract void stopInternal();

    @Override
    public void destroy() {

    }

    private synchronized void setStateInternal(String type,
                                               Object data){
        lifecycle.fireLifecycleEvent(type,data);
    }

    @Override
    public void addLifecycleListener(LifecycleListener lifecycleListener) {
        lifecycle.addLifecycleListeners(lifecycleListener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener lifecycleListener) {
        lifecycle.removeLifecycleListener(lifecycleListener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }
}
