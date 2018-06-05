/**
 * Created by lpxie on 2017/2/6.
 */
public class TestHash {
    public static void main(String[] args){


        System.out.println(Integer.parseInt("0001111", 2) & 15);
        System.out.println(Integer.parseInt("0011111", 2) & 15);
        System.out.println(Integer.parseInt("0111111", 2) & 15);
        System.out.println(Integer.parseInt("1111111", 2) & 15);
    }
}
