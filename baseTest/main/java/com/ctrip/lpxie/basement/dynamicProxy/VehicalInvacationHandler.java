package com.ctrip.lpxie.basement.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by lpxie on 2016/4/28.
 */
public class VehicalInvacationHandler implements InvocationHandler{
    private final Object vehical;

    public VehicalInvacationHandler(Object vehical){
        this.vehical = vehical;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("---before running...");
        Object ret = method.invoke(vehical,args);
        System.out.println("---after running...");
        return ret;
    }
}
