package com.ctrip.lpxie.basement.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by lpxie on 2016/4/28.
 */
public class TimeHandler implements InvocationHandler{
    private final Object object;
    public TimeHandler(Object object){
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("start time");
        Object obj = method.invoke(object, args);
        System.out.println("end time");
        return obj;
    }
}
