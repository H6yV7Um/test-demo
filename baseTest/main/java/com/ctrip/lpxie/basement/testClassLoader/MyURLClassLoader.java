package com.ctrip.lpxie.basement.testClassLoader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by lpxie on 2017/1/16.
 */
public class MyURLClassLoader extends URLClassLoader {
    private String packageName = "";

    public MyURLClassLoader(URL[] classPath,ClassLoader parent){
        super(classPath,parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> aClass = findLoadedClass(name);
        if(aClass != null){
            return aClass;
        }
        if(name.contains("lpxie")){
            return super.loadClass(name);
        }else{
            return findClass(name);
        }
    }
}
