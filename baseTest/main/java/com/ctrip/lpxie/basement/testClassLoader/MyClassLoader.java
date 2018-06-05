package com.ctrip.lpxie.basement.testClassLoader;

import java.io.*;
import java.net.URLClassLoader;

/**
 * Created by lpxie on 2017/1/16.
 */
public class MyClassLoader extends ClassLoader {
    private String classPath;

    public MyClassLoader(String classPath){
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(name.contains("lpxie")){
            byte[] classData = getData(name);
            if(classData == null)
                throw new ClassNotFoundException();
            else {
                return defineClass(name,classData,0,classData.length);
            }
        }else {
            return super.loadClass(name);
        }
    }

    private byte[] getData(String className){
        String path = classPath + File.separatorChar+className.replace('.',File.separatorChar)+".class";
        try {
            InputStream inputStream = new FileInputStream(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int num = 0;
            while ((num = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, num);
            }
            return outputStream.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
