package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardHost extends ContainerBase implements Host {
    public StandardHost(){
        super();
        pipeline.setBasic(new StandardHostValve());

        //add listener
        addLifecycleListener(new HostConfig());
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    protected void startInternal() {
        System.out.println("StandardHost startInternal");
        //parent class -> start -> sub-class startInternal
    }

    @Override
    protected void stopInternal() {

    }

    @Override
    public String[] findAliases() {
        return new String[0];
    }
}
