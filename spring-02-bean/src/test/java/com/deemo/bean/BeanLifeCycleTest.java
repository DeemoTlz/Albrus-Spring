package com.deemo.bean;

import com.deemo.bean.config.BeanLifeCycleConfiguration;
import com.deemo.bean.entity.Car4LifeCycle;
import com.deemo.bean.entity.Cat4LifeCycle;
import com.deemo.bean.entity.Dog4LifeCycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanLifeCycleTest {
    private AnnotationConfigApplicationContext applicationContext;

    @BeforeEach
    public void before() {
        this.applicationContext = new AnnotationConfigApplicationContext(BeanLifeCycleConfiguration.class);
    }

    @Test
    public void beanTest() {
        System.out.println(this.applicationContext.getBean(Car4LifeCycle.class));
    }

    @Test
    public void beanTest2() {
        System.out.println(this.applicationContext.getBean(Cat4LifeCycle.class));
    }

    @Test
    public void beanTest3() {
        System.out.println(this.applicationContext.getBean(Dog4LifeCycle.class));
    }

    @AfterEach
    public void after() {
        applicationContext.close();
    }

}
