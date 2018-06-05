package com.ctrip.lpxie.jmx;

/**
 * Created by lpxie on 2017/1/5.
 */
public class Car implements CarMBean {
    String color = "red";
    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void drive() {
        System.out.println("driving...");
    }
}
