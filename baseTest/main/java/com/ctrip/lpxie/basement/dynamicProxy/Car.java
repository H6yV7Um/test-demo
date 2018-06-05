package com.ctrip.lpxie.basement.dynamicProxy;

/**
 * Created by lpxie on 2016/4/28.
 */
public class Car implements IVehical {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email = null;
    @Override
    public void run() {
        System.out.println("Car is running");
    }

    @Override
    public void run1(){
        System.out.println("Car1 is running");
    }

    @Override
    public void run2(String name) {

    }

}
