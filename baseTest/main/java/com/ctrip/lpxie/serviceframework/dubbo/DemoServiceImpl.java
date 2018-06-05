package com.ctrip.lpxie.serviceframework.dubbo;

/**
 * Created by lpxie on 2016/8/3.
 */
public class DemoServiceImpl implements DemoService{
    @Override
    public String sayHello(String name) {
        return "Hello"+name;
    }
}
