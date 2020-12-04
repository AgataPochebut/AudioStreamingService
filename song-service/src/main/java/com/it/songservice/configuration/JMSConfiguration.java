package com.it.songservice.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JMSConfiguration {

//    @Bean(name = "topicJmsListenerContainerFactory")
//    public DefaultJmsListenerContainerFactory connectionFactory(SingleConnectionFactory connectionFactory,
//                                                                DefaultJmsListenerContainerFactoryConfigurer configurer) {
//        connectionFactory.setClientId("DurabilityTest");
//
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        configurer.configure(factory, connectionFactory);
//        // You could still override some of Boot's default if necessary.
////        factory.setPubSubDomain(true);
////        factory.setSubscriptionDurable(true);
//        return factory;
//    }

}
