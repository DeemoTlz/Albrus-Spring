package com.deemo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private AnnotationConfigApplicationContext applicationContext;

    @BeforeEach
    public void before() {
        this.applicationContext = new AnnotationConfigApplicationContext(App.class);
    }

    @Test
    public void extraTest() {
        this.applicationContext.publishEvent(new ApplicationEvent("Deemo event") {});
    }

    @AfterEach
    public void after() {
        applicationContext.close();
    }
}
