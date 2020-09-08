package com.it.songservice.component;

import com.it.songservice.annotation.Decorate;
import com.it.songservice.annotation.StorageType;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.resource.decorator.ResourceStorageDecoratorFactory;
import com.it.songservice.service.storage.song.SongStorageService;
import com.it.songservice.service.storage.song.decorator.SongStorageDecoratorFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class BeanPostProcessorImpl implements BeanPostProcessor {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private ResourceStorageDecoratorFactory resourceStorageDecoratorFactory;

    @Autowired
    private SongStorageDecoratorFactory songStorageDecoratorFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof ResourceStorageService) {
            StorageType storageType = AnnotationUtils.findAnnotation(bean.getClass(), StorageType.class);
            Decorate decorate = AnnotationUtils.findAnnotation(bean.getClass(), Decorate.class);
            if (decorate != null) {
                for (Class<?> decorator : decorate.decorations()) {
                    bean = resourceStorageDecoratorFactory.create((ResourceStorageService) bean, decorator);
                }
            }
            if (storageType != null) {
                resourceStorageServiceManager.registerService(storageType.value(), (ResourceStorageService) bean);
            }
        }

        if (bean instanceof SongStorageService) {
            Decorate decorate = AnnotationUtils.findAnnotation(bean.getClass(), Decorate.class);
            if (decorate != null) {
                for (Class<?> decorator : decorate.decorations()) {
                    bean = songStorageDecoratorFactory.create((SongStorageService) bean, decorator);
                }
            }
        }
        return bean;
    }
}
