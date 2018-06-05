package com.ctrip.lpxie.cubic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lpxie on 2017/4/1.
 */
public class SpecialReader {
    //公司名称 --- 数据行列表
    public static Map<String,List<Item>> source = new HashMap<>();

    public static void read(File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
                line = line.replace(",","");
                String[] subLines = line.split("[\\s]+");
                String companyName = subLines[0];

                String date = subLines[1];

                String p = subLines[2];
                String b = subLines[3];
                String f = subLines[4];
                Item item = new Item();
                item.setDate(date);
                item.setB(Double.parseDouble(b));
                item.setP(Double.parseDouble(p));
                item.setF(Double.parseDouble(f)/100.0);
                if(source.containsKey(companyName)){
                    source.get(companyName).add(item);
                }else{
                    List<Item> items = new ArrayList<>();
                    source.put(companyName,items);
                    items.add(item);
                }
            }
            inputStreamReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    public static void write(File file,String line){

    }
}
