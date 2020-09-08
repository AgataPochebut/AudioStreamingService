package com.it.songservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableJms
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableTransactionManagement
@EnableCaching
//@EnableSpringConfigured
//@EnableLoadTimeWeaving
public class SongServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SongServiceApplication.class, args);
    }

}
