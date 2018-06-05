package com.ctrip.lpxie.basement.dynamicProxy;

import java.lang.reflect.Proxy;

/**
 * Created by lpxie on 2016/4/28.
 */
public class Client {
    public static void main(String[] args){
        Car car = new Car();
        Class[] interfaces = new Class[]{IVehical.class};

        VehicalInvacationHandler h1 = new VehicalInvacationHandler(car);

        IVehical proxyObj =  (IVehical)Proxy.newProxyInstance(IVehical.class.getClassLoader(),interfaces,h1);

        //TimeHandler h2 = new TimeHandler(proxyInstance);//这里其实可以理解成代理的是(IVehical)proxyInstance的run 方法,不能直接代理handler的invoke方法

        //IVehical proxyObj =  (IVehical)Proxy.newProxyInstance(IVehical.class.getClassLoader(),interfaces,h2);



        proxyObj.run();
        proxyObj.run1();
        proxyObj.run2("lpxie");
        proxyObj.toString();
    }
}
