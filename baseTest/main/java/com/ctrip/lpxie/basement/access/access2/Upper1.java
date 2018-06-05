package com.ctrip.lpxie.basement.access.access2;

import com.ctrip.lpxie.basement.access.access1.B;

/**
 * Created by lpxie on 2016/5/20.
 */
public class Upper1 {
    private  String upper1 = "upper1";

    String upper3 = "upper3";

    protected String upper2 = "upper2";

    private String getUpper2(){
        return upper2;
    }

    private String getB(){
        return B.b;
    }

    private final  class innerU{
       private String accessU()
        {
            return upper1;
        }

        private String accessU2()
        {
            return upper2;
        }

        private String accessU3()
        {
            return upper3;
        }
    }

}
