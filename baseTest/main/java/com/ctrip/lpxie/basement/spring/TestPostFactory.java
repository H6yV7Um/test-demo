package com.ctrip.lpxie.basement.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lpxie on 2016/7/29.
 */
public class TestPostFactory implements BeanFactoryPostProcessor {
    public Set<String> getValue() {
        return value;
    }

    public void setValue(Set<String> value) {
        this.value = value;
    }

    public Set<String> value = new HashSet<String>();
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] names = beanFactory.getBeanDefinitionNames();
        for(String n : value){
            System.out.println(n);
        }
    }
}
