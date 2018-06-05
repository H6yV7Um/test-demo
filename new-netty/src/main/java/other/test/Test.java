package other.test;

import java.util.ArrayList;

/**
 * Created by lpxie on 2017/2/21.
 */
public class Test {
    public static void main(String[] args){
        String a = new String("1");
        String b = new String("1");
        System.out.println(a == b);
       /* int[] values1 = {1,2,5,4,7,8};
        questionOne(values1);
        int[] values2 = {9,8,3,4,1,6};
        questionTwo(values2);*/
    }

    static void questionOne(int[] values){
        int tempX = -1;//当前匹配的奇数下标
        int tempY = -1;//当前匹配的偶数下标
        for(int i=0,j=values.length-1;i<=j;){
            if(tempX == -1){
                if(values[i]%2 == 1){//奇数
                    tempX = i;
                }else{
                    i++;
                }
            }

            if(tempY == -1){
                if(values[j]%2 == 0){//偶数
                    tempY = j;
                }else{
                    j--;
                }
            }

            if(tempX != -1 && tempY != -1){
                int temp = values[tempX];
                values[tempX] = values[tempY];
                values[tempY] = temp;
                //复位
                tempX = -1;
                tempY = -1;
            }
        }
        //输出结果
        for(int i = 0;i<values.length;i++){
            System.out.println(values[i]);
        }
    }

    static void questionTwo(int[] values){
        boolean exist = false;
        int[] indexMaxValue = new int[values.length];//当前索引下的最大值
        int[] indexCount = new int[values.length];//当前索引下连续的个数【大于等于2表示找到】
        for(int i=0;i<values.length;i++){
            if(exist)
                break;
            if(i == 0){//first item
                indexMaxValue[i] = values[i];
                indexCount[i] = 0;
                continue;
            }else {
                for(int j = 0;j<i;j++){
                    if(values[i] > indexMaxValue[j]){
                        indexMaxValue[j] = values[i];
                        indexCount[j]++;
                        if(indexCount[j] == 2){
                            exist = true;//find result
                            break;
                        }
                    }
                }
                indexMaxValue[i] = values[i];
                indexCount[i] = 0;
            }
        }
        System.out.println(exist);
    }
}
