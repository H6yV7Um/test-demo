package com.ctrip.lpxie.basement.knight.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by lpxie on 2016/7/19.
 */
public abstract class ContainerBase extends LifecycleBase implements Container {
    protected Pipeline pipeline = new StandardPipeline(this);

    protected HashMap<String, Container> children =
            new HashMap<String, Container>();

    protected List<ContainerListener> listeners =
            new CopyOnWriteArrayList<ContainerListener>();

    protected Container parent = null;

    protected String name = null;

    protected ThreadPoolExecutor startStopExecutor;

    @Override
    protected void initInternal(){
        BlockingQueue<Runnable> startStopQueue =
                new LinkedBlockingQueue<Runnable>();
        startStopExecutor = new ThreadPoolExecutor(
                2,
                4, 10, TimeUnit.SECONDS,
                startStopQueue);
        startStopExecutor.allowCoreThreadTimeOut(true);
    }

    protected synchronized void startInternal(){

        // Start our subordinate components, if any


        // Start our child containers, if any
        Container children[] = findChildren();
        List<Future<Void>> results = new ArrayList<Future<Void>>();
        for (int i = 0; i < children.length; i++) {
            results.add(startStopExecutor.submit(new StartChild(children[i])));
        }

        boolean fail = false;
        for (Future<Void> result : results) {
            try {
                result.get();
            } catch (Exception e) {
                System.out.println("containerBase.threadedStartFailed"+e.getMessage());
            }

        }
    }

    public Container[] findChildren() {

        synchronized (children) {
            Container results[] = new Container[children.size()];
            return children.values().toArray(results);
        }

    }

    public void addChild(Container child) {
        addChildInternal(child);
    }

    private void addChildInternal(Container child) {
        synchronized(children) {
            if (children.get(child.getName()) != null)
                throw new IllegalArgumentException("addChild:  Child name '" +
                        child.getName() +
                        "' is not unique");
            child.setParent(this);  // May throw IAE
            children.put(child.getName(), child);
        }
    }

    public synchronized void addValve(Valve valve) {

        pipeline.addValve(valve);
        fireContainerEvent("addValve",this);
    }

    public Container getParent() {
        return parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public Pipeline getPipeline() {
        return this.pipeline;
    }


    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    // ----------------------------- Inner classes used with start/stop Executor

    private static class StartChild implements Callable<Void> {

        private Container child;

        public StartChild(Container child) {
            this.child = child;
        }

        @Override
        public Void call() throws Exception {
            child.start();
            return null;
        }
    }

    public void addContainerListener(ContainerListener listener) {
        listeners.add(listener);
    }

    public void fireContainerEvent(String type, Object data) {

        if (listeners.size() < 1)
            return;

        ContainerEvent event = new ContainerEvent(this, type, data);
        // Note for each uses an iterator internally so this is safe
        for (ContainerListener listener : listeners) {
            listener.containerEvent(event);
        }
    }
}
