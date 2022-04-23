package com.deemo.bean.config;

import com.deemo.bean.entity.Bird4LifeCycle;
import com.deemo.bean.entity.Car4LifeCycle;
import com.deemo.bean.entity.Cat4LifeCycle;
import com.deemo.bean.entity.Dog4LifeCycle;
import com.deemo.bean.postprocessor.DeemoBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanLifeCycleConfiguration {

    /**
     * 默认：方法名即为容器中的ID
     * 也可以在注解 @Bean("BYD-汉") 指定
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public Car4LifeCycle car4LifeCycle() {
        System.out.println("create car...");
        Car4LifeCycle car = new Car4LifeCycle();
        car.setName("汉 DM-P");
        return car;
    }

    @Bean
    public Cat4LifeCycle cat4LifeCycle() {
        System.out.println("create cat...");
        return new Cat4LifeCycle();
    }

    @Bean
    public Dog4LifeCycle dog4LifeCycle() {
        System.out.println("create dog...");
        return new Dog4LifeCycle();
    }

    @Bean
    public DeemoBeanPostProcessor deemoBeanPostProcessor() {
        return new DeemoBeanPostProcessor();
    }

    @Bean
    public Bird4LifeCycle bird4LifeCycle() {
        System.out.println("create bird...");
        return new Bird4LifeCycle();
    }

}
