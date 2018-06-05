package com.ctrip.lpxie.basement.finalTest;

/**
 * Created by lpxie on 2017/1/18.
 */
public class FinalMain {
    public static void main(String[] args){
        MyFinalClass myFinalClass = new MyFinalClass();
        myFinalClass.age = 23;
        myFinalClass.testFinal();

        ExtendFinalClass extendFinalClass = new ExtendFinalClass();
        extendFinalClass  = null;
    }
}
