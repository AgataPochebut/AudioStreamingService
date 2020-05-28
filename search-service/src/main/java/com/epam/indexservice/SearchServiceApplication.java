package com.epam.indexservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableElasticsearchRepositories(includeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE, classes = ElasticsearchRepository.class))
@EnableFeignClients
//@EnableDiscoveryClient
@EnableJms
public class SearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchServiceApplication.class, args);
	}

}
