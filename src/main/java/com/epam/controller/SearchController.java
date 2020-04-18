package com.epam.controller;

import com.epam.dto.response.SongResponseDto;
import com.epam.model.Song;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

//    @Autowired
//    private GenericSearchServiceES<Song> service;

    @Autowired
    private Mapper mapper;

    @GetMapping("/songs/{keyword}")
    public ResponseEntity<List<SongResponseDto>> searchSongs(@PathVariable String keyword){
//        List<Song> entity = service.search(query);

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        List<Song> entity = elasticsearchTemplate.queryForList(searchQuery, Song.class);

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
