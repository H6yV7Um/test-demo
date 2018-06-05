package com.ctrip.lpxie.basement.knight;

import com.ctrip.lpxie.basement.knight.structure.*;
import com.ctrip.lpxie.basement.knight.servlet.Filter;
import com.ctrip.lpxie.basement.knight.servlet.FilterDef;
import com.ctrip.lpxie.basement.knight.servlet.Servlet;

/**
 * this is the startup class for myself tomcat
 * Created by lpxie on 2016/7/18.
 */
public class Fire {
    public static void main(String[] args){
        //server and service
        StandardServer server = new StandardServer();
        StandardService service = new StandardService();
        server.addService(service);
        service.setServer(server);

        Connector connector = new Connector(service);

        //engine and host
        StandardEngine engine = new StandardEngine();
        engine.setService(service);
        service.setContainer(engine);
        StandardHost host = new StandardHost();
        host.addValve(new HostLogValve());
        host.setParent(engine);
        engine.addChild(host);

        service.addConnector(connector);


       StandardContext context = new StandardContext();
     try {
         Class servlet = Class.forName("com.ctrip.lpxie.basement.knight.application.SelfServlet");
        Class filter = Class.forName("com.ctrip.lpxie.basement.knight.application.SelfFilter");
         Class filter1 = Class.forName("com.ctrip.lpxie.basement.knight.application.TestFilter");
       StandardWrapper wrapper = new StandardWrapper();
       wrapper.setServlet((Servlet)servlet.newInstance());
       wrapper.setParent(context);
      context.addChild(wrapper);
      FilterDef filterDef = new FilterDef();
      filterDef.setFilter((Filter)filter.newInstance());
      filterDef.setFilterClass(filter.getName());

         FilterDef filterDef1 = new FilterDef();
         filterDef1.setFilter((Filter)filter1.newInstance());
         filterDef1.setFilterClass(filter1.getName());

     context.getFilterDefs().put(filter.getName(),filterDef);
         //context.getFilterDefs().put(filter1.getName(),filterDef1);
     } catch (Exception e) {
      e.printStackTrace();
     }

     host.addChild(context);

     server.init();

     server.start();


        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.getAdapter().setConnector(connector);
        connectionServer.startInternal();
        System.out.println("fire successfully");
    }
}
