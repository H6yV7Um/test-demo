package com.ctrip.lpxie.jmx;

import javax.management.*;

/**
 * Created by lpxie on 2017/1/5.
 */
public class StandardAgent {
    private MBeanServer mBeanServer = null;

    public StandardAgent(){
        mBeanServer = MBeanServerFactory.createMBeanServer();
    }

    public MBeanServer getBeanServer(){
        return mBeanServer;
    }

    public ObjectName createObjectName(String name){
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(name);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return objectName;
    }

    private void createStandardBean(ObjectName objectName,String managedResourceClassName){
        try {
            mBeanServer.createMBean(managedResourceClassName,objectName);
        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        /*ObjectName adapterName = new ObjectName("CarAgent:name=htmladapter,port=8082");
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
        server.registerMBean(adapter,adapterName);
        adapter.start();*/

        StandardAgent agent = new StandardAgent();
        MBeanServer mBeanServer = agent.getBeanServer();
        String domain = mBeanServer.getDefaultDomain();
        String managedResourceClassName = "com.ctrip.lpxie.jmx.Car";
        ObjectName objectName = agent.createObjectName(domain+":type="+managedResourceClassName);
        agent.createStandardBean(objectName,managedResourceClassName);

        Attribute colorAttribute = new Attribute("Color","blue");

        try {
            mBeanServer.setAttribute(objectName,colorAttribute);
            System.out.println(mBeanServer.getAttribute(objectName,"Color"));
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }
}
