package com.ctrip.lpxie.basement.dynamicProxy;

/**
 * Created by lpxie on 2016/4/28.
 */
public class Lorry implements IVehical {
    @Override
    public void run() {
        System.out.println("lorry run");
    }

    @Override
    public void run1() {
        System.out.println("lorry run1");
    }

    @Override
    public void run2(String name) {
        System.out.println("lorry run2"+name);
    }
}
