package com.ctrip.lpxie.cubic;

import java.util.Scanner;

/**
 * Created by lpxie on 2017/4/1.
 */
public class SolveSimpleEquation implements I3 {
    public void SolveSimpleEquation()
    {
        System.out.println("请输入形如一元一次方程ax+b=0的两个系数");
        Scanner sce=new Scanner(System.in);
        double a=sce.nextDouble();
        double b=sce.nextDouble();
        if(a==0)
        {
            System.out.print("输入错误!");
        }
        else
        {
            System.out.println("方程的根是x="+(-b/a));
        }
    }
}
