package com.ctrip.lpxie.basement;

/**
 * Created by lpxie on 2016/5/12.
 */
public class SomeTest {

    public static void main(String[] args){
        int i = 1;
        switch (i){
            case 0:
            case 1:
                System.out.println(0);
                //System.out.println(1);
                break;
            case 2:
                System.out.println(2);
                break;
        }
        String url = "http://ws.flowtable4j.infosec.ctripcorp.com/flowtable4j/rest/checkAFS";
        String subUrl = url.substring(url.indexOf("/",7),url.length());
        String cp = new SomeTest().getClass().getClassLoader().getResource("").toString();
        String cp1 = System.getProperty("java.class.path");
        String[] ips = "171.116.238.176:0".trim().split(":");
        String ip = ips[0];
        String ipHead = ip.split("\\.")[0];

        long now = System.currentTimeMillis();
        long phase = (long)30*24*60*60 * 1000;//3个月
        final long checkTime = now-phase;

        System.out.println("Mozilla/5.0 (iPhone; CPU iPhone OS 8_4 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12H143 MicroMessenger/6.3.1".length());
        int h = 0xffffffff;
        int h1 = h >>> 20;
        int h2 = h >>> 12;
        int h3 = h1 ^ h2;
        int h4 = h ^ h3;
        int h5 = h4 >>> 7;
        int h6 = h4 >>> 4;
        int h7 = h5 ^ h6;
        int h8 = h4 ^ h7;

        printBin ( h );
        printBin ( h1);
        printBin ( h2 );
        printBin ( h3 );
        printBin ( h4 );
        printBin ( h5 );
        printBin ( h6 );
        printBin ( h7 );
        printBin ( h8 );
        System.out.println(h);
        System.out.println(h1);
        System.out.println(h2);
        System.out.println(h3);
        System.out.println(h4);
        System.out.println(h5);
        System.out.println(h6);
        System.out.println(h7);
        System.out.println(h8);

        printBin(12);
        printBin(32);
        printBin(33 & 32);

        System.out.println(12 & 32);
    }

    static void printBin(int h){
        System.out.println(String.format("%32s",Integer.toBinaryString(h)).replace(' ','0'));
    }
}
