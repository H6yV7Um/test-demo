package com.ctrip.lpxie.basement;

import java.io.UnsupportedEncodingException;

/**
 * Created by lpxie on 2016/5/18.
 */
public class ReplaceTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String hotelName = "厦门梦幻之屋客栈 name ? ';$gaga gdsgdsg";
        hotelName = new String(hotelName.getBytes(),"utf-8");
        hotelName = hotelName.replaceAll("[\u4e00-\u9fa5]+", "");
        String newName = "";
        for(int i = 0;i<hotelName.length();i++){
            if(Character.isAlphabetic(hotelName.charAt(i))){
                newName += hotelName.charAt(i);
            }else
            {
                newName += " ";
            }
        }
        newName = newName.replaceAll("[\\s]+"," ").trim();
        hotelName = newName;
        System.out.println(hotelName);
    }
}
