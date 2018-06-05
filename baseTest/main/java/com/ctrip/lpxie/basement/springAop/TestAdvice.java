package com.ctrip.lpxie.basement.springAop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by lpxie on 2016/7/30.
 */
public class TestAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("i am advice before:"+invocation.getClass().getSimpleName()+"-"+invocation.getMethod().getName());
        invocation.proceed();
        System.out.println("i am advice after:" + invocation.getClass().getSimpleName()+"-" + invocation.getMethod().getName());
        return "TestAdvice";
    }
}
