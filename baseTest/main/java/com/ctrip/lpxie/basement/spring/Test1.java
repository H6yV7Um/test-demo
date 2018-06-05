package com.ctrip.lpxie.basement.spring;

/**
 * Created by lpxie on 2016/7/28.
 */
public class Test1 implements Ts{

    private String name = null;
    private String email = null;

    public String getName() {
        System.out.println("I am in getName");
        return name;
    }

    public void setName(String name) {
        System.out.println("I am in setName");
        this.name = name;
    }
}
