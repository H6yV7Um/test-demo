package com.ctrip.lpxie.basement.annotation;

/**
 * Created by lpxie on 2016/12/29.
 */
@MyTarget(world = "shanghai",array = {4,5},style = Integer.class)
public class MyTest {
    @MyTarget(world = "newyue",array = {6,7},style = String.class)
    @SuppressWarnings("test")
    @Deprecated
    public void output(){
        System.out.println("output something!");
    }
}
