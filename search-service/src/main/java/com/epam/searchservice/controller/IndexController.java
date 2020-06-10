package com.epam.searchservice.controller;

import org.dozer.Mapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    @Autowired
    private Mapper mapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void create(Model model) throws Exception {
//        IndexRequest request = new IndexRequest();
//        request.index("service");
//        request.type(model.getAttribute("type"));
//        request.id(message.getStringProperty("id"));
//        request.source(message.getObjectProperty("source"), XContentType.JSON);
//        elasticsearchClient.index(request, RequestOptions.DEFAULT);
        System.out.println(model);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(Model model) throws IOException {
//        DeleteRequest request = new DeleteRequest();
//        request.index("service");
//        request.type(message.getStringProperty("type"));
//        request.id(message.getStringProperty("id"));
//        elasticsearchClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(model);
    }
}
