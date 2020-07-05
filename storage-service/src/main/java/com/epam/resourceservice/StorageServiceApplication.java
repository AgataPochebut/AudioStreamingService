package com.epam.resourceservice;

import com.epam.resourceservice.annotation.Decorate;
import com.epam.resourceservice.annotation.StorageType;
import com.epam.resourceservice.service.repository.ResourceRepositoryService;
import com.epam.resourceservice.service.storage.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;


//1. jmstemplate
//2. invoker + interface
@SpringBootApplication
@EnableJpaRepositories
        (includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
@EnableCaching
@EnableJms
@EnableFeignClients
@EnableDiscoveryClient
public class StorageServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApplication.class, args);
    }

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;


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
                        newbean = new CleanupDecorator(newbean, repositoryService);
                        newbean = new DedupingDecorator(newbean, repositoryService);
                        newbean = new CacheDecorator(newbean, cacheManager);
                    }
                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
                        resourceStorageFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
                    }
                    return newbean;
                }

                return bean;
            }
        };
    }
}
