package com.deemo.bean.factory;

import com.deemo.bean.entity.Bird4Import;
import org.springframework.beans.factory.FactoryBean;

public class BeanFactoryBean implements FactoryBean<Bird4Import> {
    @Override
    public Bird4Import getObject() throws Exception {
        System.out.println("factory create bird...");
        Bird4Import bird4Import = new Bird4Import();
        bird4Import.setColor("五颜六色");
        return bird4Import;
    }

    @Override
    public Class<?> getObjectType() {
        return Bird4Import.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
