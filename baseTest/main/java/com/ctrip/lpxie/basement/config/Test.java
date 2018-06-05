package com.ctrip.lpxie.basement.config;

import java.io.FileInputStream;
import java.net.URL;

/**
 * Created by lpxie on 2016/4/28.
 */
public class Test {
    public static void main(String[] args) throws Exception{
        URL url = Test.class.getClassLoader().getResource("config.properties");
        String path = url.getPath();
        IConfig config = ConfigFactory.create(new FileInputStream(path));
        String dbUrl = config.dbUrl();
        boolean isValidate = config.isValidated();
        int poolSize = config.poolSize();

        System.out.println(dbUrl+"\t"+isValidate+"\t"+poolSize);
    }
}
