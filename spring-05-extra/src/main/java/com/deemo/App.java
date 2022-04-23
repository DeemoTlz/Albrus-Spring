package com.deemo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Hello world!
 */
@Configuration
@ComponentScan("com.deemo")
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
