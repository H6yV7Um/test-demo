package com.ctrip.lpxie.basement.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lpxie on 2016/12/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface MyTarget {
    String world() default "earth";
    int[] array() default {1,2,3};
    Class style() default String.class;
}
