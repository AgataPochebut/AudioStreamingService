package com.epam.audiostreamingservice.configuration;

import org.springframework.context.annotation.Configuration;

//какой клиент по умолчанию? boot сам создает клиента и темплейт
@Configuration
public class ElasticSearchConfiguration {

//    @Autowired
//    private RestHighLevelClient elasticsearchClient;

//    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
//    public ElasticsearchOperations elasticsearchOperations() {
//        return new ElasticsearchRestTemplate(elasticsearchClient);
//    }

}

