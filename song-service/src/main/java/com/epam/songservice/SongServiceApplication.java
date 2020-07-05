package com.epam.songservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories
        (includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
@EnableCaching
@EnableAsync
@EnableJms
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.epam.resourceservice"})
public class SongServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SongServiceApplication.class, args);
    }

    //    @Autowired
//    private ResourceStorageFactory resourceStorageFactory;

//    @Autowired
//    private ResourceRepositoryService resourceRepositoryService;

//    @Autowired
//    private ConversionClient conversionService;

//    @Autowired
//    private Producer producer;

//    @Autowired
//    private CacheManager cacheManager;
//
//
//    @Autowired
//    private SongIndexClient songIndexClient;
//
//    @Autowired
//    private Mapper mapper;
//
//    @Autowired
//    private SongRepositoryService songRepositoryService;
//
//    @Bean
//    public BeanPostProcessor entityManagerBeanPostProcessor1() {
//        return new BeanPostProcessor() {
//
//            @Override
//            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//
////                if (bean instanceof ResourceStorageService) {
////                    ResourceStorageService newbean = (ResourceStorageService) bean;
////                    if (bean.getClass().isAnnotationPresent(Decorate.class)
////                            && bean.getClass().getAnnotation(Decorate.class).value() == ResourceStorageDecorator.class) {
//////                        newbean = new IORetryDecorator(newbean);
////                        newbean = new DBDecorator(newbean, resourceRepositoryService);
//////                        newbean = new CleanupDecorator(newbean, resourceRepositoryService);
////                        newbean = new DedupingDecorator(newbean, resourceRepositoryService);
//////                        newbean = new ConversionDecorator(newbean, jmsTemplate);
//////                        newbean = new CacheDecorator(newbean, cacheManager);
////                    }
////                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
////                        resourceStorageFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
////                    }
////                    return newbean;
////                }
//
//                if (bean instanceof SongStorageService) {
//                    SongStorageService newbean = (SongStorageService) bean;
//                    if (bean.getClass().isAnnotationPresent(Decorate.class)
//                            && bean.getClass().getAnnotation(Decorate.class).value() == SongStorageDecorator.class) {
//                        newbean = new SongMetadataDecorator(newbean, mapper);
//                        newbean = new SongDBDecorator(newbean, songRepositoryService);
////                        newbean = new SongIndexDecorator(newbean, jmsTemplate);
////                        newbean = new SongCleanupDecorator(newbean, resourceRepositoryService);
//                        newbean = new SongDedupingDecorator(newbean, songRepositoryService);
////                        newbean = new SongConversionDecorator(newbean, producer);
//                    }
//                    return newbean;
//                }
//
//                return bean;
//            }
//        };
//    }
}
