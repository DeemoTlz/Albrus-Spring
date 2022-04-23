package com.deemo;

import com.deemo.aspect.LogAspect;
import com.deemo.service.IGameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

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
    public void aopTest() {
        System.out.println(this.applicationContext.getBean(LogAspect.class));
    }

    @Test
    public void txTest() {
        IGameService gameService = this.applicationContext.getBean(IGameService.class);
        System.out.println(gameService.insert(UUID.randomUUID().toString().substring(0, 5), 99.0));
        System.out.println(gameService.insert(UUID.randomUUID().toString().substring(0, 5), 99.0, true));
    }

    @AfterEach
    public void after() {
        applicationContext.close();
    }
}
