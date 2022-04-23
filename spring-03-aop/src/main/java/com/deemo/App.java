package com.deemo;

import com.deemo.aspect.CalculatorAspect;
import com.deemo.calculator.DeemoCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Hello world!
 */
@Configuration
@EnableAspectJAutoProxy
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    @Bean
    public DeemoCalculator deemoCalculator() {
        return new DeemoCalculator();
    }

    @Bean
    public CalculatorAspect calculatorAspect() {
        return new CalculatorAspect();
    }

}
