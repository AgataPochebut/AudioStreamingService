package com.epam.songservice;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.feign.index.IndexClient;
import com.epam.songservice.service.repository.*;
import com.epam.songservice.service.storage.Song.*;
import com.epam.songservice.service.storage.Resource.*;
import org.dozer.Mapper;
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
import org.springframework.scheduling.annotation.EnableAsync;

//1. jmstemplate
//2. invoker + interface
@SpringBootApplication
@EnableJpaRepositories
        (includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
//@EntityScan({"com.epam.commonservice.model"})
@EnableCaching
@EnableAsync
@EnableJms
@EnableFeignClients
@EnableDiscoveryClient
public class SongServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SongServiceApplication.class, args);
    }

    @Autowired
    private ResourceRepositoryService resourceRepositoryService;

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
    private CacheManager cacheManager;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;


    @Autowired
    private IndexClient indexService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private SongRepositoryService songRepositoryService;

    @Bean
    public BeanPostProcessor entityManagerBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

                if (bean instanceof ResourceStorageService){
                    ResourceStorageService newbean = (ResourceStorageService) bean;
                    if (bean.getClass().isAnnotationPresent(Decorate.class)
                            && bean.getClass().getAnnotation(Decorate.class).value() == ResourceStorageDecorator.class) {
//                        newbean = new IORetryDecorator(newbean);
                        newbean = new DBDecorator(newbean, resourceRepositoryService);
//                        newbean = new CleanupDecorator(newbean, resourceRepositoryService);
                        newbean = new DedupingDecorator(newbean, resourceRepositoryService);
//                        newbean = new ConversionDecorator(newbean, jmsTemplate);
//                        newbean = new CacheDecorator(newbean, cacheManager);
                    }
                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
                        resourceStorageFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
                    }
                    return newbean;
                }

                else if (bean instanceof SongStorageService){
                    SongStorageService newbean = (SongStorageService) bean;
                    if (bean.getClass().isAnnotationPresent(Decorate.class)
                            && bean.getClass().getAnnotation(Decorate.class).value() == SongStorageDecorator.class) {
//                        newbean = new MetadataDecorator(newbean, mapper);
                        newbean = new SongDBDecorator(newbean, songRepositoryService);
//                        newbean = new IndexDecorator(newbean, jmsTemplate);
                        newbean = new SongDedupingDecorator(newbean, songRepositoryService);
                    }
                    return newbean;
                }

                return bean;
            }
        };
    }

//    @Bean
//    @Primary
//    ResourceStorageService resourceStorageService() {
//        return resourceStorageFactory.getService();
//    }

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
