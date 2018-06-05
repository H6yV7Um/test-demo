package com.ctrip.lpxie.basement.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lpxie on 2016/8/9.
 */
public class TestLog {
    private static  Logger logger = LoggerFactory.getLogger(TestLog.class);
    public static void main(String[] args) throws InterruptedException {
        if(logger.isDebugEnabled()){
            logger.debug("debug is enabled");
        }
//        this.join();
        logger.info("I am info");
        logger.warn("I am warn");
        logger.error("I am error");
        /*Thread.sleep(20000);
        if(logger.isDebugEnabled()){
            logger.debug("debug is enabled");
        }
        logger.info("I am info");
        logger.warn("I am warn");
        logger.error("I am error");*/

        byte[] bytes = new byte[4];
        bytes[0] = (10>>24)&0xFF;
        bytes[1] = (10>>16)&0xFF;
        bytes[2] = (10>>8)&0xFF;
        bytes[3] = 10&0xFF;
        System.out.println(Integer.toBinaryString(10));
        System.out.println(Integer.toBinaryString(bytes[0]));
        System.out.println((10>>16)&0xFF);
        System.out.println((10>>8)&0xFF);
        System.out.println(10&0xFF);

    }
}
