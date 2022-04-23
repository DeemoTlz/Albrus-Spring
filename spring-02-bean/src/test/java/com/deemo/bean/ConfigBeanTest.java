package com.deemo.bean;

import com.deemo.bean.config.BeanConfiguration;
import com.deemo.bean.entity.Car;
import com.deemo.bean.entity.OperationSystem;
import com.deemo.bean.service.impi.AlbrusServiceImpl;
import com.deemo.bean.service.impi.DeemoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConfigBeanTest {

    @Test
    public void configFileTest() {
        try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml")) {
            Car car = applicationContext.getBean(Car.class);
            System.out.println(car);
        }
    }

    @Test
    public void annotationConfigTest() {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfiguration.class)) {
            Car car = applicationContext.getBean(Car.class);
            System.out.println(car);
        }
    }

    @Test
    public void lazyLoadTest() {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfiguration.class)) {
            /*Car car = applicationContext.getBean(Car.class);
            System.out.println(car);*/

            DeemoServiceImpl deemoService = applicationContext.getBean(DeemoServiceImpl.class);
            System.out.println(deemoService.getCar());
            System.out.println(deemoService.getCar());
            AlbrusServiceImpl albrusService = applicationContext.getBean(AlbrusServiceImpl.class);
            System.out.println(albrusService.getCar());
            System.out.println(albrusService.getCar());
            System.out.println(albrusService.getCar());

            System.out.println(deemoService.getCar().getClass());
            System.out.println(albrusService.getCar().getClass());
        }
    }

    @Test
    public void conditionTest() {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfiguration.class)) {
            System.out.println(applicationContext.getBean(OperationSystem.class));
        }
    }

}
