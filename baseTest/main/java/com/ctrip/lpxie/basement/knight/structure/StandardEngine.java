package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardEngine extends ContainerBase implements Engine {
    private Service service;

    public StandardEngine(){
        super();//if super class has some initialized job to do ,then do it first
        pipeline.setBasic(new StandardEngineValve());//the last execute engineValue in order
    }

    public void addChild(Container child) {
        if (!(child instanceof Host))
            throw new IllegalArgumentException
                    (("standardEngine.notHost"));
        super.addChild(child);

    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    @Override
    protected void initInternal() {
        System.out.println("StandardEngine initInternal");
        super.initInternal();

    }

    @Override
    protected void startInternal() {
        System.out.println("StandardEngine startInternal");

        // Standard container startup
        super.startInternal();
    }

    @Override
    protected void stopInternal() {

    }

    @Override
    public String getName() {
        return name;
    }
}
