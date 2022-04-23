package com.deemo.bean.entity;

import lombok.Data;

@Data
public class Car4LifeCycle {
    private Long id;
    private String name;
    private String brand;

    public void init() {
        System.out.println("car init...");
    }

    public void destroy() {
        System.out.println("car destroy...");
    }

}
