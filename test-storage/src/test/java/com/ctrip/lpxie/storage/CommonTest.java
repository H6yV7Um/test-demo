package com.ctrip.lpxie.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpxie on 2017/3/23.
 */
public class CommonTest {
    @Test
    public void testListRemove(){
        String strR = "84777908307510067209031092111344229353F20006313072962368369779128536223.104.11.249ee50501201cf60917b58c6084e113aad2D5864010702017-03-31 19:54:30.943CP0001002_2579918728792_8970ead1-79fe-443f-9cac-c5550661acc9";
        strR = strR.trim();
        List<Long> items = new ArrayList<>();
        items.add(10L);
        items.add(11L);
        items.add(12L);
        items.add(13L);
        items.add(14L);
        items.remove(10);
        items.remove(10L);
        long i = 11;
        long i1 = 13L;
        items.remove(i);
        items.remove(i1);
        System.out.println("");
    }
}
