package com.ctrip.lpxie.basement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by lpxie on 2016/5/13.
 */
public class HashMapTest {
    public static void main(String[] args){
        final HashMap map = new HashMap();
        Map test = map;
        test.put("ddd","ddddd");
        test.put(12,"ddddsdgd");
        Set set =  test.keySet();
        /*Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            Object o = iterator.next();
        }*/
    }
}
