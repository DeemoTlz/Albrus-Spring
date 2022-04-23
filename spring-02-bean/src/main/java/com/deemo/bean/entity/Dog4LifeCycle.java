package com.deemo.bean.entity;

import lombok.Data;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Data
public class Dog4LifeCycle {
    private Long id;
    private String name;
    private String brand;

    @PostConstruct
    public void init() {
        System.out.println("dog init...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("dog destroy...");
    }

}
