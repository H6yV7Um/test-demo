import com.ctrip.lpxie.basement.spring.Test1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lpxie on 2016/7/30.
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/test.xml")*/
public class TestMain {
    @Autowired
    //@Qualifier()
    Test1 test1;

    @Test
    public void getTest1() {
            System.out.println(19%8);
    }

    private void tt(boolean is)
    {
        System.out.println(is);
        is = true;
        System.out.println(is);
    }
    @Test
    public void test(){
        String regEx = "[0-9]{9}";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher("D123456789".substring(1,10));
        if(mat.find()){
            System.out.println("true");
            System.out.println("D123456789".substring(1, 10));

        } else {
            System.out.println("false");

        }
    }
}
