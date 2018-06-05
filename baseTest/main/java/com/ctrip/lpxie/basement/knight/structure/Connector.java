package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class Connector extends LifecycleBase{

    protected Mapper mapper = new Mapper();

    protected MapperListener mapperListener = new MapperListener(mapper, this);

    protected Service service = null;


    public Request createRequest() {
        Request request = new Request();
        return (request);

    }

    public Response createResponse() {
        Response response = new Response();
        return (response);
    }

    public Connector(Service service){
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    protected void initInternal() {

    }

    @Override
    protected void startInternal() {
        mapperListener.start();
    }

    @Override
    protected void stopInternal() {

    }

    public Mapper getMapper() {
        return (mapper);
    }
}
