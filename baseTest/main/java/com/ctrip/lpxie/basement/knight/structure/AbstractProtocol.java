package com.ctrip.lpxie.basement.knight.structure;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lpxie on 2016/7/19.
 */
public class AbstractProtocol<S> {
    private ConcurrentHashMap<S,Processor<S>> connections = new ConcurrentHashMap<S, Processor<S>>();//when to add socket and processor into this map

    private void process(S s){
        Processor processor = connections.get(s);
        processor.doProcess(s);
    }
}
