package com.it.songservice.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.SocketUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@Profile("test")
public class ActiveMQTestConfiguration {

    private int port;
    private BrokerService broker;

    public ActiveMQTestConfiguration() throws Exception {
        port = SocketUtils.findAvailableTcpPort();
        broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector("tcp://localhost:" + port);
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        broker.start();
    }

    @PreDestroy
    public void preDestroy() throws Exception {
        broker.stop();
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory n = new ActiveMQConnectionFactory("tcp://localhost:" + port);
        n.setTrustAllPackages(true);
        return n;
    }

}

