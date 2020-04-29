package com.epam.indexservice.controller;//package com.epam.streamingservice.controller;
//
//import com.epam.streamingservice.dto.response.SongResponseDto;
//import com.epam.streamingservice.model.Song;
//import org.dozer.Mapper;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
////мб тут возвращать все типы?
//@RestController
//@RequestMapping("/search")
//public class SearchController {
//
//    @Autowired
//    private Mapper mapper;
//
//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchTemplate;
//
//    @GetMapping("/songs/{keyword}")
//    public ResponseEntity<List<SongResponseDto>> searchSongs(@PathVariable String keyword){
//
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .build();
//
//        List<Song> entity = elasticsearchTemplate.queryForList(searchQuery, Song.class);
//
//        final List<SongResponseDto> responseDto = entity.stream()
//                .map((i) -> mapper.map(i, SongResponseDto.class))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//}
