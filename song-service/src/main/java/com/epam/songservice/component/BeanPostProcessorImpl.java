package com.epam.songservice.component;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.feign.conversion.ConversionClient;
import com.epam.songservice.feign.index.SongIndexClient;
import com.epam.songservice.service.repository.ResourceRepositoryService;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.resource.*;
import com.epam.songservice.service.storage.song.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanPostProcessorImpl implements BeanPostProcessor {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private ResourceRepositoryService resourceRepositoryService;

    @Autowired
    private ConversionClient conversionClient;

    @Autowired
    private SongIndexClient songIndexClient;

    @Autowired
    private SongRepositoryService songRepositoryService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof ResourceStorageService) {
            ResourceStorageService newbean = (ResourceStorageService) bean;
            if (bean.getClass().isAnnotationPresent(Decorate.class)
                    && bean.getClass().getAnnotation(Decorate.class).value() == ResourceStorageDecorator.class) {
                newbean = new ResourceIORetryDecorator(newbean);
                newbean = new ResourceDBDecorator(newbean, resourceRepositoryService);
//                newbean = new ResourceCacheDecorator(newbean, cacheManager);
            }
            if (bean.getClass().isAnnotationPresent(StorageType.class)) {
                resourceStorageServiceManager.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
            }
            return newbean;
        }

        if (bean instanceof SongStorageService) {
            SongStorageService newbean = (SongStorageService) bean;
            if (bean.getClass().isAnnotationPresent(Decorate.class)
                    && bean.getClass().getAnnotation(Decorate.class).value() == SongStorageDecorator.class) {
                newbean = new SongConversionDecorator(newbean, conversionClient);
                newbean = new SongDBDecorator(newbean, songRepositoryService);
                newbean = new SongIndexDecorator(newbean, songIndexClient);
            }
            return newbean;
        }

        return bean;
    }
}
