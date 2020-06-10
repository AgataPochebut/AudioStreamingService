package com.epam.searchservice.controller;

import com.epam.searchservice.dto.response.SearchResponseDto;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private Mapper mapper;

    @GetMapping("/client/{keyword}")
    public ResponseEntity<List<SearchResponseDto>> searchClient(@PathVariable String keyword) throws IOException {

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchRequest searchRequest = new SearchRequest("service");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        List<SearchResponseDto> responseDto = new ArrayList<>();
        searchResponse.getHits().iterator().forEachRemaining(hit->responseDto.add(mapper.map(hit, SearchResponseDto.class)));

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/template/{keyword}")
    public ResponseEntity<List<SearchResponseDto>> searchTemplate(@PathVariable String keyword) throws IOException {

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("service")
                .withQuery(queryBuilder)
                .build();

        List<SearchResponseDto> responseDto = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<List<SearchResponseDto>>() {
            @Override
            public List<SearchResponseDto> extract(SearchResponse response) {
                List<SearchResponseDto> responseDto = new ArrayList<>();
                response.getHits().iterator().forEachRemaining(hit->responseDto.add(mapper.map(hit, SearchResponseDto.class)));
                return responseDto;
            }
        });

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
