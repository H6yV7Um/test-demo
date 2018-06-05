package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class DefaultAdapter {
    private Connector connector;

    public DefaultAdapter(){
        super();
    }

    public void service(Request request,Response response)
    {
        //do parse host - context

        //Request request1 = connector.createRequest();
        request.getMappingData().host = connector.getMapper().getHost();
        request.getMappingData().context = connector.getMapper().getContext();
        request.getMappingData().wrapper = connector.getMapper().getWrapper();
        Response response1 = connector.createResponse();

        //call the container service
        connector.getService().getContainer().getPipeline().getFirst().invoke(request,response);
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }
}
