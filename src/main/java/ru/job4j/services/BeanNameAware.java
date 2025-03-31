package ru.job4j.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanNameAware implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void displayBeanName() {
        String beanName = applicationContext.getBeanNamesForType(this.getClass())[0];
        System.out.println("Bean name: " + beanName);
    }
}
