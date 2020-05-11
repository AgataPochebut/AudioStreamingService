//package com.epam.indexservice.controller;
//
//import com.epam.commonservice.dto.response.SongResponseDto;
//import com.epam.indexservice.model.Song;
//import org.bouncycastle.math.raw.Mod;
//import org.dozer.Mapper;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//
//@RestController
//@RequestMapping("/index/songs")
//public class IndexController {
//
//    @Autowired
//    private RestHighLevelClient elasticsearchClient;
//
//    @Autowired
//    private Mapper mapper;
//
//    @PostMapping
//    @ResponseStatus(value = HttpStatus.OK)
//    public void create(Model model) throws Exception {
//        Song entity = mapper.map(requestDto, Song.class);
//        entity = service.save(entity);
//
//        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
//
//        IndexRequest request = new IndexRequest();
//        request.index("service");
//        request.type(model.getAttribute("type"));
//        request.id(message.getStringProperty("id"));
//        request.source(message.getObjectProperty("source"), XContentType.JSON);
//        elasticsearchClient.index(request, RequestOptions.DEFAULT);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    @ResponseStatus(value = HttpStatus.OK)
//    public void delete(@PathVariable Long id) {
//        service.deleteById(id);
//    }
//}
