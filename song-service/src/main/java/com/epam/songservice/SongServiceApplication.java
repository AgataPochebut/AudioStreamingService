package com.epam.songservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableJms
@EnableDiscoveryClient
@EnableFeignClients
public class SongServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SongServiceApplication.class, args);
    }

//    @Autowired
//    private ResourceStorageFactory resourceStorageFactory;
//
//    @Autowired
//    private ResourceRepositoryService resourceRepositoryService;
//
//    @Autowired
//    private ConversionClient conversionClient;
//
//    @Autowired
//    private SongIndexClient songIndexClient;
//
//    @Autowired
//    private Producer producer;
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Autowired
//    private SongRepositoryService songRepositoryService;
//
//    @Bean
//    public BeanPostProcessor entityManagerBeanPostProcessor() {
//        return new BeanPostProcessor() {
//
//            @Override
//            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//
//                if (bean instanceof ResourceStorageService) {
//                    ResourceStorageService newbean = (ResourceStorageService) bean;
//                    if (bean.getClass().isAnnotationPresent(Decorate.class)
//                            && bean.getClass().getAnnotation(Decorate.class).value() == ResourceStorageDecorator.class) {
//                        newbean = new ResourceIORetryDecorator(newbean);
//                        newbean = new ResourceDBDecorator(newbean, resourceRepositoryService);
////                        newbean = new ResourceCacheDecorator(newbean, cacheManager);
//                    }
//                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
//                        resourceStorageFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
//                    }
//                    return newbean;
//                }
//
//                if (bean instanceof SongStorageService) {
//                    SongStorageService newbean = (SongStorageService) bean;
//                    if (bean.getClass().isAnnotationPresent(Decorate.class)
//                            && bean.getClass().getAnnotation(Decorate.class).value() == SongStorageDecorator.class) {
//                        newbean = new SongConversionDecorator(newbean, conversionClient, resourceStorageFactory);
//                        newbean = new SongDBDecorator(newbean, songRepositoryService);
//                        newbean = new SongIndexDecorator(newbean, songIndexClient);
//                    }
//                    return newbean;
//                }
//
//                return bean;
//            }
//        };
//    }

}
