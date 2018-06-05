package com.ctrip.lpxie.basement.testClassLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by lpxie on 2017/1/16.
 */
public class TestMain {
    static MyClassLoader myClassLoader = new MyClassLoader("D:\\workspace\\innerTest\\target\\classes");

    static MyURLClassLoader myURLClassLoader = null;//new MyURLClassLoader({new URL("D:\\workspace\\innerTest\\target\\classes")},null);
    public static void main(String[] args){
        try {
            File test = new File("D:\\workspace\\innerTest\\target\\classes\\");
            URI uri =  test.toURI();
            URL url = uri.toURL();
            URL[] ls = {url};
            myURLClassLoader = new MyURLClassLoader(ls, new URLClassLoader(ls));
            Class<?> loadedClass = myURLClassLoader.findClass("com.ctrip.lpxie.basement.testClassLoader.TestEntity");
            Object entity = loadedClass.newInstance();
            for(Field field : entity.getClass().getDeclaredFields()){
                field.setAccessible(true);
                System.out.println(field.getName()+"="+field.get(entity));
            }
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
