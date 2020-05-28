package com.epam.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableZuulProxy
@EnableJms
@EnableFeignClients
@EnableDiscoveryClient
@EnableHystrixDashboard
public class GatewayApplication {

//	@Bean
//	public Encoder encoder() {
//		return new FormEncoder();
//	}

//	@Bean
//	public Encoder springEncoder() {
//		ObjectFactory<HttpMessageConverters> objectFactory = () ->
//				new HttpMessageConverters(new FormHttpMessageConverter());
//		return new SpringEncoder(objectFactory);
//	}

//	@Bean
//	public Encoder encoder() {
//		return new GsonEncoder();
//	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

//	OAuth2AuthenticationToken
}
