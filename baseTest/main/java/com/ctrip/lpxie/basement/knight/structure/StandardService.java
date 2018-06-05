package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardService extends LifecycleBase implements Service{
    Server server = null;
    Connector[] connectors = new Connector[0];
    Container container = null;


    public void addConnector(Connector connector){
        Connector[] result = new Connector[connectors.length+1];
        for(int i=0;i<connectors.length;i++){
            result[i] = connectors[i];
        }
        result[connectors.length] = connector;
        connectors = result;
    }

    public Connector findConnector(){
        return connectors[0];//fixme return first connector
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    protected void initInternal() {
        System.out.println("StandardService initInternal");
        container.init();
    }

    @Override
    protected void startInternal() {
        System.out.println("StandardService startInternal");
        for (Connector connector: connectors) {
            connector.start();
        }
        container.start();
    }

    @Override
    protected void stopInternal() {

    }
}
