package com.ctrip.lpxie.basement.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Created by lpxie on 2016/7/28.
 */
public class TestAware implements BeanFactoryAware {
    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        System.out.println("setBeanFactory");
    }

    public void testAware(){
        Test1 test1 = (Test1)beanFactory.getBean("test1");
        test1.setName("testAware");
        System.out.println(test1.getName());
    }
}
