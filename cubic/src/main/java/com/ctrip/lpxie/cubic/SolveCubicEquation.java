package com.ctrip.lpxie.cubic;

import java.util.Scanner;

/**
 * Created by lpxie on 2017/4/1.
 */
public class SolveCubicEquation implements I1 {
    public void SolveCubicEquation()
    {
        System.out.println("请输入形如一元三次方程mx^3+nx^2+tx+s=0的四个系数");
        Scanner sce=new Scanner(System.in);
        double m=sce.nextDouble();
        double n=sce.nextDouble();
        double t=sce.nextDouble();
        double s=sce.nextDouble();
        if(m==0)
        {
            System.out.print("输入错误!");
        }
        else
        {
            double a=n/m;
            double b=t/m;
            double c=s/m;
            double q=(a*a-3*b)/9;
            double r=(2*a*a*a-9*a*b+27*c)/54;
            if(r*r<q*q*q)
            {
                System.out.println("此方程有三个解:");
                t=Math.acos(r/Math.sqrt(q*q*q));
                double x1=-2*Math.sqrt(q)*Math.cos(t/3)-a/3;
                double x2=-2*Math.sqrt(q)*Math.cos((t+2*Math.PI)/3)-a/3;
                double x3=-2*Math.sqrt(q)*Math.cos((t-2*Math.PI)/3)-a/3;
                System.out.println("x1="+x1+","+"x2="+x2+","+"x3="+x3);
            }
            else
            {
                System.out.println("此方程只有一个解:");
                int sgn=(r>=0)?1:-1;
                double u=-sgn*Math.pow((Math.abs(r)+Math.sqrt(r*r-q*q*q)),1./3);
                double v=(u!=0)?q/u:0;
                double x1=u+v-a/3;
                System.out.println("x="+x1);
            }
        }
    }

    public static double getResult(double m,double n,double t,double s){
        double results = 0.0;
        if(m==0)
        {
            System.out.print("输入错误!");
            return results;
        }else {
            double a=n/m;
            double b=t/m;
            double c=s/m;
            double q=(a*a-3*b)/9;
            double r=(2*a*a*a-9*a*b+27*c)/54;
            if(r*r<q*q*q)
            {
                System.out.println("此方程有三个解:\n");
                t=Math.acos(r/Math.sqrt(q*q*q));
                double x1=-2*Math.sqrt(q)*Math.cos(t/3)-a/3;
                double x2=-2*Math.sqrt(q)*Math.cos((t+2*Math.PI)/3)-a/3;
                double x3=-2*Math.sqrt(q)*Math.cos((t-2*Math.PI)/3)-a/3;
                System.out.println("x1=" + x1 + "," + "x2=" + x2 + "," + "x3=" + x3);

                if(x1 > 0 && x2 >0){
                    results = Math.min(x1,x2);
                }
                if(x1 > 0 && x3 >0){
                    results = Math.min(x1,x3);
                }
                if(x2 > 0 && x3 >0){
                    results = Math.min(x2,x3);
                }
                if(results == 0.0){
                    if(x1 > 0)
                        results = x1;
                    if(x2 > 0)
                        results = x2;
                    if(x3 > 0)
                        results = x3;
                }

            }
            else
            {
                System.out.println("此方程只有一个解:");
                int sgn=(r>=0)?1:-1;
                double u=-sgn*Math.pow((Math.abs(r)+Math.sqrt(r*r-q*q*q)),1./3);
                double v=(u!=0)?q/u:0;
                double x1=u+v-a/3;
//                System.out.println("x=" + x1);
                results = x1;
            }
            System.out.print("final = "+results+"\n");
            return results;
        }
    }
}
