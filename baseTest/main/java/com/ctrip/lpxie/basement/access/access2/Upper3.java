package com.ctrip.lpxie.basement.access.access2;

/**
 * Created by lpxie on 2016/5/20.
 */
public class Upper3 {
    private String accessU2()
    {
        return new Upper2().upper2;
    }

    private String accessU3()
    {
        return new Upper1().upper3;
    }
}
