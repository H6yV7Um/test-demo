package com.ctrip.lpxie.basement.spring;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by lpxie on 2016/7/28.
 */

public class Application {
    public static void main(String[] args){
//        ApplicationContext bf = new ClassPathXmlApplicationContext("spring/test.xml");
//        bf.getBean("test1");
        //bf.getBean("test2");

        /*TestAware testAware = bf.getBean("testAware",TestAware.class);
        testAware.testAware();*/

        Resource resource = new ClassPathResource("spring/test.xml");
        ConfigurableListableBeanFactory bf = new XmlBeanFactory(resource);
       /* TestPostFactory d = (TestPostFactory)bf.getBean("bfp");
        d.postProcessBeanFactory(bf);*/

        //bf.addBeanPostProcessor(bf.getBean("test1",BeanPostProcessor.class));

        Ts test1 = (Ts)bf.getBean("test1");
        test1.setName("test1");
        System.out.println(test1.getName());

    }
}
