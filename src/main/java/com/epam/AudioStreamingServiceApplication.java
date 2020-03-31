package com.epam;

import com.epam.annotation.Decorate;
import com.epam.annotation.StorageType;
import com.epam.service.repository.ResourceRepositoryService;
import com.epam.service.storage.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AudioStreamingServiceApplication {

    public static void main(String[] args) { SpringApplication.run(AudioStreamingServiceApplication.class, args); }

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private ResourceStorageFactory storageServiceFactory;

    @Bean
    public BeanPostProcessor entityManagerBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ResourceStorageService){
                    ResourceStorageService newbean = (ResourceStorageService) bean;
                    if (bean.getClass().isAnnotationPresent(Decorate.class)
                            && bean.getClass().getAnnotation(Decorate.class).value() == ResourceStorageDecorator.class) {
                        newbean = new IORetryDecorator(newbean);
                        newbean = new DBInsertDecorator(newbean, repositoryService);
                        newbean = new DedupingDecorator(newbean, repositoryService);
                    }
                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
                        storageServiceFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
                    }
                }
                return bean;
            }
        };
    }

}
