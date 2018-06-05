package com.ctrip.lpxie.basement.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lpxie on 2016/7/28.
 */
public class MPX extends ClassPathXmlApplicationContext {
    public MPX(String... con){
        super(con);
    }

    protected void initPropertySources(){
        getEnvironment().setRequiredProperties("VAR");
    }
}
