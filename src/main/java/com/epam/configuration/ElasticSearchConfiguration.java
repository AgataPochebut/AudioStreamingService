package com.epam.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

//какой клиент по умолчанию?
@Configuration
public class ElasticSearchConfiguration {

    //    @Bean
//    public RestHighLevelClient elasticsearchClient() {
//        return RestClients.create(ClientConfiguration.create("localhost:9200")).rest();
//    }

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchOperations elasticsearchOperations() {
        return new ElasticsearchRestTemplate(elasticsearchClient);
    }

}

