package com.ctrip.lpxie.basement.finalTest;

/**
 * Created by lpxie on 2017/1/18.
 */
public final class ExtendFinalClass extends MyFinalClass {
    ExtendFinalClass(){
        id = 1;
//        name = "";
    }

    @Override
    public void testFinal() {
        super.testFinal();
    }
}
