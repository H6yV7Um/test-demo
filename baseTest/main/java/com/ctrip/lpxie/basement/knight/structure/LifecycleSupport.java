package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/20.
 */
public final class LifecycleSupport {
    private Lifecycle lifecycle = null;

    public LifecycleSupport(Lifecycle lifecycle){
        super();
        this.lifecycle = lifecycle;
    }

    private LifecycleListener[] listeners = new LifecycleListener[0];

    private final Object listenersLock = new Object(); // Lock object for changes to listeners

    public void addLifecycleListeners(LifecycleListener lifecycleListener){
        synchronized (listenersLock){
            LifecycleListener[] results = new LifecycleListener[listeners.length+1];
            for(int i = 0;i<listeners.length;i++){
                results[i] = listeners[i];
            }
            results[listeners.length] = lifecycleListener;
            listeners = results;
        }
    }

    public LifecycleListener[] findLifecycleListeners(){
        return listeners;
    }

    public void fireLifecycleEvent(String type,Object data){
        LifecycleEvent lifecycleEvent = new LifecycleEvent(lifecycle,type,data);
        LifecycleListener interested[] = listeners;//fixme why do this ?
        for(int i= 0;i<interested.length;i++)
            interested[i].lifecycleEvent(lifecycleEvent);
    }

    /**
     * Remove a lifecycle event listener from this component.
     *
     * @param listener The listener to remove
     */
    public void removeLifecycleListener(LifecycleListener listener) {

        synchronized (listenersLock) {
            int n = -1;
            for (int i = 0; i < listeners.length; i++) {
                if (listeners[i] == listener) {
                    n = i;
                    break;
                }
            }
            if (n < 0)
                return;
            LifecycleListener results[] =
                    new LifecycleListener[listeners.length - 1];
            int j = 0;
            for (int i = 0; i < listeners.length; i++) {
                if (i != n)
                    results[j++] = listeners[i];
            }
            listeners = results;
        }

    }
}
