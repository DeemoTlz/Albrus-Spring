package com.deemo.bean.config;

import com.deemo.bean.condition.LinuxCondition;
import com.deemo.bean.condition.WindowsCondition;
import com.deemo.bean.entity.Car;
import com.deemo.bean.entity.OperationSystem;
import com.deemo.bean.entity.Pig4Import;
import com.deemo.bean.filter.DeemoTypeFilter;
import com.deemo.bean.factory.BeanFactoryBean;
import com.deemo.bean.selector.BeanImportBeanDefinitionRegistrar;
import com.deemo.bean.selector.BeanImportSelector;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(value = "com.deemo.bean", excludeFilters = {
        // 排除 Controller
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class}),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = DeemoTypeFilter.class)
})
@Import({Pig4Import.class, BeanImportSelector.class, BeanImportBeanDefinitionRegistrar.class})
public class BeanConfiguration {

    /**
     * 默认：方法名即为容器中的ID
     * 也可以在注解 @Bean("BYD-汉") 指定
     */
    @Lazy
    @Bean
    public Car car() {
        System.out.println("create car...");
        Car car = new Car();
        car.setName("汉 DM-P");
        return car;
    }

    @Bean
    @Conditional({LinuxCondition.class})
    public OperationSystem linux() {
        System.out.println("create linux os...");
        OperationSystem linux = new OperationSystem();
        linux.setName("Linux");
        return linux;
    }

    @Bean
    @Conditional({WindowsCondition.class})
    public OperationSystem windows() {
        System.out.println("create windows os...");
        OperationSystem windows = new OperationSystem();
        windows.setName("Windows");
        return windows;
    }

    @Bean
    public BeanFactoryBean beanFactoryBean() {
        return new BeanFactoryBean();
    }

}
