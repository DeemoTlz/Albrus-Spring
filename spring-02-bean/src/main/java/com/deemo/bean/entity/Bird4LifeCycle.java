package com.deemo.bean.entity;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Data
public class Bird4LifeCycle implements ApplicationContextAware {
    private Long id;
    private String name;
    private String brand;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("setApplicationContext...");
        this.applicationContext = applicationContext;
    }
}
