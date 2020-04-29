package com.epam.indexservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

//    public List<T> search(String keyword){
//
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .build();
//
//        return (List<T>) elasticsearchTemplate.queryForList(searchQuery, .getClass());
//    }
}
