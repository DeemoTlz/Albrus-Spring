package com.deemo.bean.filter;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class DeemoTypeFilter implements TypeFilter {
    /**
     * @param metadataReader 可以用来获取 当前类 的元信息
     * @param metadataReaderFactory 可以用来获取 任意类 的元信息
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 当前类的注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        // 当前类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        // 当前类的类资源信息（路径、、、）
        Resource resource = metadataReader.getResource();

        MetadataReader metadataReader1 = metadataReaderFactory.getMetadataReader("com.deemo.bean.entity.Car");
        MetadataReader metadataReader2 = metadataReaderFactory.getMetadataReader(resource);

        return false;
    }
}
