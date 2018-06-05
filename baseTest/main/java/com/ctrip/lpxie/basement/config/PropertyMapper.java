package com.ctrip.lpxie.basement.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by lpxie on 2016/4/28.
 */
public class PropertyMapper implements InvocationHandler {
    private final Properties properties;

    public PropertyMapper(Properties properties){
        this.properties = properties;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        final Value value = method.getAnnotation(Value.class);
        if(value == null)
            return null;

        //start this are our logical codes
        String property = properties.getProperty(value.value());
        //end

        if(property == null)
            return null;

        final Class<?> returns = method.getReturnType();
        if(returns.isPrimitive()){
            if(returns.equals(int.class))
                return Integer.valueOf(property);
            else if (returns.equals(long.class))
                return (Long.valueOf(property));
            else if (returns.equals(double.class))
                return (Double.valueOf(property));
            else if (returns.equals(float.class))
                return (Float.valueOf(property));
            else if (returns.equals(boolean.class))
                return (Boolean.valueOf(property));
        }
        return property;
    }
}
