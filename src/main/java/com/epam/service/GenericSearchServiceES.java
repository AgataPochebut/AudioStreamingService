//package com.epam.service;
//
//import com.epam.dto.response.SongResponseDto;
//import com.epam.model.Song;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@Service
//public class GenericSearchServiceES<T>{
//
//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchTemplate;
//
//    public List<T> search(@RequestParam String query){
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .build();
//
//        return elasticsearchTemplate.queryForList(searchQuery, (Class)T);
//    }
//
//}
