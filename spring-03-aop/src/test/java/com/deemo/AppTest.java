package com.deemo;

import com.deemo.calculator.DeemoCalculator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    public void calculatorTest() {
        DeemoCalculator calculator = applicationContext.getBean(DeemoCalculator.class);
        System.out.println(calculator.div(2, 4));
        System.out.println(calculator.div(2, 0));
    }

    @AfterEach
    public void after() {
        applicationContext.close();
    }

}
