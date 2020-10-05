//package com.it.songservice.configuration;
//
//import com.it.songservice.component.JMSErrorHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.jms.annotation.EnableJms;
//import org.springframework.jms.annotation.JmsListenerConfigurer;
//import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//import org.springframework.jms.config.JmsListenerContainerFactory;
//import org.springframework.jms.config.JmsListenerEndpointRegistrar;
//
//import javax.jms.ConnectionFactory;
//
//@EnableJms
//@Slf4j
//@Configuration
//@Profile("test")
//public class ActiveMQTestConfiguration {
//
//    private int port;
//    private BrokerService broker;
//
//    public ActiveMQTestConfiguration() throws Exception {
//        port = SocketUtils.findAvailableTcpPort();
//        broker = new BrokerService();
//        broker.setPersistent(false);
//        broker.setUseJmx(false);
//        broker.addConnector("tcp://localhost:" + port);
//    }
//
//    @PostConstruct
//    public void postConstruct() throws Exception {
//        broker.start();
//    }
//
//    @PreDestroy
//    public void preDestroy() throws Exception {
//        broker.stop();
//    }
//
//    @Bean
//    public ActiveMQConnectionFactory connectionFactory() {
//        ActiveMQConnectionFactory n = new ActiveMQConnectionFactory("tcp://localhost:" + port);
//        n.setTrustAllPackages(true);
//        return n;
//    }
//}
