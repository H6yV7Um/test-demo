package com.ctrip.lpxie.basement.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * Created by lpxie on 2016/4/28.
 */
public class ConfigFactory {
    private ConfigFactory(){}

    public static IConfig create(final InputStream in) throws IOException{
        final Properties properties = new Properties();
        properties.load(in);

        return (IConfig)Proxy.newProxyInstance(IConfig.class.getClassLoader(),new Class[]{IConfig.class},new PropertyMapper(properties));
    }
}
