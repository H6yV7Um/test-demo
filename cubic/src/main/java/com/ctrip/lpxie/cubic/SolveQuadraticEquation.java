package com.ctrip.lpxie.cubic;

import java.util.Scanner;

/**
 * Created by lpxie on 2017/4/1.
 */
public class SolveQuadraticEquation implements I2 {
    public void SolveQuadraticEquation()
    {
        System.out.println("请输入形如一元二次方程ax^2+bx+c=0的三个系数");
        Scanner sce=new Scanner(System.in);
        double a=sce.nextDouble();
        double b=sce.nextDouble();
        double c=sce.nextDouble();
        if(a==0)
        {
            System.out.print("输入错误!");
        }
        else
        {
            double d=b*b-4*a*c;
            if(d>0)
            {
                double x1=(-b+Math.sqrt(d))/2*a;
                double x2=(-b+Math.sqrt(d))/2*a;                                                                                             System.out.println("有两个实根x1="+x1+","+"x2="+x2);
            }
            else if(d==0)
            {
                System.out.println("有一个实根"+(-b/2*a));
            }
            else if(d<0)
            {
                double Re=-b/(2*a);
                double Im=Math.sqrt(-d)/(2*a);
                System.out.println("实部="+Re+"    "+"虚部="+Im);
            }
        }
    }
}
