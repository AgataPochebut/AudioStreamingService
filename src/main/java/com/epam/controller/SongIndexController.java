package com.epam.controller;

import com.epam.model.Song;
import com.epam.service.index.SongRepositoryService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/index/songs")
public class SongIndexController {

    @Autowired
    private SongRepositoryService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<Song>> find() {
        final List<Song> entity = service.findAll();

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Song> find(@PathVariable Long id) {
        Song entity = service.findById(id);

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Song> save(@Valid @RequestBody Song entity) throws Exception {
        service.save(entity);

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Song> update(@PathVariable Long id, @Valid @RequestBody Song entity) throws Exception {
        service.update(entity);

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        final Song entity = service.findById(id);
        service.delete(entity);
    }


//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchTemplate;
//
//    @GetMapping("/template")
//    public void readAll1(){
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .build();
//
//        elasticsearchTemplate.queryForList(searchQuery, Song.class);
//    }
//
//    @GetMapping("/template/{id}")
//    public void read1(@PathVariable String id){
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .build();
//
//        elasticsearchTemplate.queryForList(searchQuery, Song.class);
//    }
//
//    @PostMapping("/template/{id}")
//    public void create1(@PathVariable String id, @RequestBody Song entity){
//
//        IndexQuery indexQuery = new IndexQueryBuilder()
//                .withId(id)
//                .withObject(entity)
//                .build();
//
//        elasticsearchTemplate.index(indexQuery);
//    }
//
//    @PutMapping(value = "/template/{id}")
//    public void update1(@PathVariable Long id, @RequestBody Song entity) throws Exception {
//
//    }
//
//    @DeleteMapping(value = "/template/{id}")
//    @ResponseStatus(value = HttpStatus.OK)
//    public void delete1(@PathVariable Long id) {
//        final Song entity = service.findById(id);
//        service.delete(entity);
//    }

}
