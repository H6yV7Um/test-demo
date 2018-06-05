package com.ctrip.lpxie.basement.knight.structure;

/**
 * just doProcess socket
 * Created by lpxie on 2016/7/19.
 */
public interface Processor<S> {
    void doProcess(S s);
}
