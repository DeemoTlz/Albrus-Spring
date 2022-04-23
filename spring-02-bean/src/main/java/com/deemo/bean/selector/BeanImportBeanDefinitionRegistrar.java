package com.deemo.bean.selector;

import com.deemo.bean.entity.Cat4Import;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

public class BeanImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry current bean definition registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println(Arrays.toString(registry.getBeanDefinitionNames()));
        if (registry.containsBeanDefinition("com.deemo.bean.entity.Pig4Import") && registry.containsBeanDefinition("com.deemo.bean.entity.Dog4Import")) {
            System.out.println("register cat...");
            registry.registerBeanDefinition("cat", new RootBeanDefinition(Cat4Import.class));
        }
    }
}
