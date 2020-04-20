//package com.epam.service.index;
//
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TemplateServiceImpl implements TemplateService {
//
//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchTemplate;
//
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
//}
