package com.ctrip.lpxie.basement.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by lpxie on 2016/12/29.
 */
public class MyTargetTest {
    public static void main(String[] args) throws Exception{
        MyTest myTest = new MyTest();
        Class<MyTest> c = MyTest.class;
        Method method = c.getMethod("output", new Class[]{});
        if(MyTest.class.isAnnotationPresent(MyTarget.class)){
            System.out.println("have annotation");
        }

        if(method.isAnnotationPresent(MyTarget.class)){
            method.invoke(myTest,null);

            MyTarget myTarget = method.getAnnotation(MyTarget.class);
            String world = myTarget.world();
            System.out.println("world="+world);
            System.out.println(myTarget.array().length);
            System.out.println(myTarget.style());
        }

        Annotation[] annotations = method.getAnnotations();
        for(Annotation annotation: annotations){
            System.out.println(annotation.annotationType().getName());
        }
    }
}
