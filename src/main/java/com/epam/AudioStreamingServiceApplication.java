package com.epam;

import com.epam.annotation.Decorate;
import com.epam.service.repository.ResourceRepositoryService;
import com.epam.service.storage.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class AudioStreamingServiceApplication {

    public static void main(String[] args) { SpringApplication.run(AudioStreamingServiceApplication.class, args); }

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Bean
    public BeanPostProcessor entityManagerBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ResourceStorageService
                        && bean.getClass().isAnnotationPresent(Decorate.class)
                        && bean.getClass().getAnnotation(Decorate.class).value() == ResourceStorageDecorator.class) {
                    ResourceStorageService storageService = (ResourceStorageService) bean;
                    storageService = new IORetryDecorator(storageService);
                    storageService = new DBInsertDecorator(storageService, repositoryService);
                    storageService = new DedupingDecorator(storageService, repositoryService);
                    bean = storageService;
                }
                return bean;
            }
        };
    }

}
