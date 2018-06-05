package com.ctrip.lpxie.basement.config;

import java.lang.annotation.*;

/**
 * Created by lpxie on 2016/4/28.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    String value();
}
