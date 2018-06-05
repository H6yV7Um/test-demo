package com.ctrip.lpxie.basement.config;

/**
 * Created by lpxie on 2016/4/28.
 */
public interface IConfig {
    @Value("db.url")
    String dbUrl();

    @Value("db.validation")
    boolean isValidated();

    @Value("db.pool.size")
    int poolSize();
}
