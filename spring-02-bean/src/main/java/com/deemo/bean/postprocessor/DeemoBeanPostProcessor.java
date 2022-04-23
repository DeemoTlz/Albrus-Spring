package com.deemo.bean.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DeemoBeanPostProcessor implements BeanPostProcessor {

    /**
     * Apply this BeanPostProcessor to the given new bean instance
     * before any bean initialization callbacks (like InitializingBean's afterPropertiesSet or a custom init-method).
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization...");
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    /**
     * Apply this BeanPostProcessor to the given new bean instance
     * after any bean initialization callbacks (like InitializingBean's afterPropertiesSet or a custom init-method).
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization...");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

}
