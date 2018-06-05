package com.ctrip.lpxie.basement.finalTest;

/**
 * Created by lpxie on 2017/1/18.
 */
public class MyFinalClass {
    final String name;
    int id;
    int age;
    long currentTime;

    public MyFinalClass(){
        name = "";
    }

    public void testFinal(){
        System.out.println("I am in testFinal method");
    }
}
