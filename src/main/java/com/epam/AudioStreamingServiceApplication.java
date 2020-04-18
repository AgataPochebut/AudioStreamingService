package com.epam;

import com.epam.annotation.Decorate;
import com.epam.annotation.StorageType;
import com.epam.service.conversion.ConversionService;
import com.epam.service.repository.ResourceRepositoryService;
import com.epam.service.storage.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
@EnableElasticsearchRepositories(includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = ElasticsearchRepository.class))
@EnableFeignClients
//@EnableDiscoveryClient
//@EnableAsync
public class AudioStreamingServiceApplication {

    public static void main(String[] args) { SpringApplication.run(AudioStreamingServiceApplication.class, args); }

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private ConversionService conversionService;

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
                        newbean = new ConversionDecorator(newbean, conversionService);
                    }
                    if (bean.getClass().isAnnotationPresent(StorageType.class)) {
                        storageServiceFactory.registerService(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
                    }
                    return newbean;
                }
                return bean;
            }
        };
    }

//    @Bean
//    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(2);
//        executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("GithubLookup-");
//        executor.initialize();
//        return executor;
//    }

//    @LoadBalanced
//    @Bean
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

}
