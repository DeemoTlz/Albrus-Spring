package com.deemo.bean;

import com.deemo.bean.config.BeanConfiguration;
import com.deemo.bean.entity.Bird4Import;
import com.deemo.bean.entity.Cat4Import;
import com.deemo.bean.entity.Dog4Import;
import com.deemo.bean.entity.Pig4Import;
import com.deemo.bean.factory.BeanFactoryBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ImportBeanTest {
    private AnnotationConfigApplicationContext applicationContext;

    @BeforeEach
    public void before() {
        this.applicationContext = new AnnotationConfigApplicationContext(BeanConfiguration.class);
    }

    @Test
    public void importTest() {
        System.out.println(this.applicationContext.getBean(Pig4Import.class));
    }

    @Test
    public void importSelectorTest() {
        System.out.println(this.applicationContext.getBean(Dog4Import.class));
    }

    @Test
    public void importBeanDefinitionRegistrarTest() {
        System.out.println(this.applicationContext.getBean(Cat4Import.class));
    }

    @Test
    public void beanFactoryBeanTest() {
        System.out.println(this.applicationContext.getBean(BeanFactoryBean.class));
        System.out.println(this.applicationContext.getBean(Bird4Import.class));
        System.out.println(this.applicationContext.getBean("beanFactoryBean"));
        System.out.println(this.applicationContext.getBean("&beanFactoryBean"));
    }

    @AfterEach
    public void after() {
        applicationContext.close();
    }

}
