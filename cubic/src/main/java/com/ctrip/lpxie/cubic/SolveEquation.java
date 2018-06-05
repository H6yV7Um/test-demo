package com.ctrip.lpxie.cubic;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by lpxie on 2017/4/1.
 */
public class SolveEquation {
    void print()throws IOException {
        char ch = 'y';
        System.out.println("求解几次方程？1：一次 2：二次 3：三次");
        Scanner sLine = new Scanner(System.in);
        int pm = sLine.nextInt();
        if (pm == 1) {
            System.out.println("你选择的是一元一次方程:");
            SolveSimpleEquation fc = new SolveSimpleEquation();
            fc.SolveSimpleEquation();
        } else if (pm == 2) {
            System.out.println("你选择的是一元二次方程:");
            SolveQuadraticEquation fc = new SolveQuadraticEquation();
            fc.SolveQuadraticEquation();
        } else if (pm == 3) {
            System.out.println("你选择的是一元三次方程:");
            SolveCubicEquation fc = new SolveCubicEquation();
            fc.SolveCubicEquation();
        } else {
            print();
        }
        System.out.println("你是否想继续:(y/n)");
        ch = (char) System.in.read();
        System.in.skip(2);
        if (ch == 'y') {
            print();
        } else if (ch == 'n') {
            System.out.println("Good luck!");
        } else {
            print();
        }
    }
    public static void main (String args[]) throws IOException
    {
        SolveEquation se=new SolveEquation();
        se.print();
    }
}
