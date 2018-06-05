package com.ctrip.lpxie.cubic;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.ctrip.lpxie.cubic.SolveCubicEquation.*;
/**
 * Created by lpxie on 2017/4/1.
 */
public class GetParameters {
    double Pt;
    double Bt;
    double Bt1;
    double Bt2;
    double Ft;
    double Ft1;
    double Ft2;
    double Ft3;

    //ax^3+bx^2+cx+d=0;
    private static double transfer(double Pt,double Bt,double Bt1,double Bt2,double Ft1,double Ft2,double Ft3){
        double a,b,c,d;
        a = Pt+Bt;
        b = -(Bt*Ft1+Bt-Bt1);
        c = -(Bt1*Ft2+Bt1-Bt2);
        d = -(Bt+Bt2+Bt2*Ft3);
        double result = getResult(a,b,c,d);

        return result;
    }

    public GetParameters(double Pt,double Bt,double Bt1,double Bt2,double Ft1,double Ft2,double Ft3){
        this.Pt = Pt;

        this.Bt = Bt;
        this.Bt1 = Bt1;
        this.Bt2 = Bt2;

        this.Ft1 = Ft1;
        this.Ft2 = Ft2;
        this.Ft3 = Ft3;
    }

    public static void main(String[] args){
        String sourceFileName = GetParameters.class.getClassLoader().getResource("").getFile()+"source";
        File sourceFile = new File(sourceFileName);
        if(!sourceFile.exists())
            try {
                sourceFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        SpecialReader.read(sourceFile);

        String targetFileName = GetParameters.class.getClassLoader().getResource("").getFile()+"target";
        File targetFile = new File(targetFileName);
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile,true);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream,"utf-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //对source进行小处理
        for(Map.Entry<String,List<Item>> entry : SpecialReader.source.entrySet()){
            String company = entry.getKey();
            List<Item> items = entry.getValue();
            for(Item item : items){
                int index = items.indexOf(item);
                int index1 = index + 1;
                int index2 = index +2;
                int index3 = index +3;
                Item item1 = new Item();
                Item item2 = new Item();
                Item item3 = new Item();
                if(index1 < items.size())
                    item1 = items.get(index1);
                if(index2 < items.size())
                    item2 = items.get(index2);
                if(index3 < items.size())
                    item3 = items.get(index3);
                double result = transfer(item.b,item.b,item1.b,item2.b,item1.f,item2.f,item3.f);
//                System.out.print("final结果:"+result);
                String finalResult = result+"\n";
                try {
                    bufferedWriter.write(finalResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
