package com.epam.songservice;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.feign.conversion.ConversionClient;
import com.epam.songservice.jms.ConversionService;
import com.epam.songservice.service.repository.ResourceRepositoryService;
import com.epam.songservice.service.storage.*;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
@EnableFeignClients
//@EnableDiscoveryClient
public class SongServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SongServiceApplication.class, args);
    }

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private ConversionClient conversionService;

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
                        newbean = new ConversionDecorator(newbean, conversionService);
                        newbean = new CacheDecorator(newbean, cacheManager);
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

    @Bean
    Queue queue() {
        return new ActiveMQQueue("testQueue");
    }

    @Bean
    FactoryBean invoker(@Qualifier("jmsConnectionFactory") ConnectionFactory factory, Queue queue) {
        JmsInvokerProxyFactoryBean factoryBean = new JmsInvokerProxyFactoryBean();
        factoryBean.setConnectionFactory(factory);
        factoryBean.setServiceInterface(ConversionService.class);
        factoryBean.setQueue(queue);
        return factoryBean;
    }

//    @Autowired
//    private JmsMessagingTemplate jmsMessagingTemplate;
//
//
//    public void send() {
//        System.out.println("send ");
//        jmsMessagingTemplate.convertAndSend(this.queue, "新发送的消息");
//    }
}
