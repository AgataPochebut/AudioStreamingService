package com.epam.songservice;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.service.repository.ResourceRepositoryService;
import com.epam.songservice.service.storage.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

//1. jmstemplate
//2. invoker + interface
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
@EnableFeignClients
//@EnableDiscoveryClient
@EnableJms
public class SongServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SongServiceApplication.class, args);
    }

    @Autowired
    private ResourceRepositoryService repositoryService;

//    @Autowired
//    private ConversionClient conversionService;
//
//    //or
//
//    @LoadBalanced
//    @Bean
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageFactory storageServiceFactory;

    @Autowired
    private CacheManager cacheManager;

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
                        newbean = new ConversionDecorator(newbean, jmsTemplate);
                        newbean = new CacheDecorator(newbean, cacheManager);
                    }
                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
                        storageServiceFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
                    }
                    return newbean;
                }
//                else if (bean instanceof SongStorageService){
//                    SongStorageService newbean = (SongStorageService) bean;
//                    {
//                        newbean = new IORetryDecorator(newbean);
//                        newbean = new DBInsertDecorator(newbean, repositoryService);
//                        newbean = new DedupingDecorator(newbean, repositoryService);
//                        newbean = new ConversionDecorator(newbean, jmsTemplate);
//                        newbean = new CacheDecorator(newbean, cacheManager);
//                    }
//                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
//                        storageServiceFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
//                    }
//                    return newbean;
//                }
                return bean;
            }
        };
    }

    @Bean
    @Primary
    ResourceStorageService resourceStorageService() {
        return storageServiceFactory.getService();
    }

//    @Bean
//    Queue queue() {
//        return new ActiveMQQueue("testQueue");
//    }
//
//    @Bean
//    FactoryBean invoker(@Qualifier("jmsConnectionFactory") ConnectionFactory factory, Queue queue) {
//        JmsInvokerProxyFactoryBean factoryBean = new JmsInvokerProxyFactoryBean();
//        factoryBean.setConnectionFactory(factory);
//        factoryBean.setServiceInterface(ConversionService.class);
//        factoryBean.setQueue(queue);
//        return factoryBean;
//    }
}
