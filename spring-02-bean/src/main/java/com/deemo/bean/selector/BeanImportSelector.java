package com.deemo.bean.selector;

import com.deemo.bean.entity.Dog4Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class BeanImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        System.out.println(importingClassMetadata.getAnnotationTypes());
        return new String[]{Dog4Import.class.getTypeName()};
    }
}
