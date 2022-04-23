package com.deemo.bean.entity;

import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Data
public class Cat4LifeCycle implements InitializingBean, DisposableBean {
    private Long id;
    private String name;
    private String brand;

    /**
     * Invoked by the containing BeanFactory after it has set all bean properties and satisfied BeanFactoryAware, ApplicationContextAware etc.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("cat afterPropertiesSet...");
    }

    @Override
    public void destroy() {
        System.out.println("cat destroy...");
    }

}
