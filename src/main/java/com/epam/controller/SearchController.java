package com.epam.controller;

import com.epam.dto.response.SongResponseDto;
import com.epam.model.Song;
import com.epam.service.search.SongSearchService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SongSearchService elasticsearchService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<SongResponseDto>> readAll() throws Exception {
        Iterable<Song> iterable = elasticsearchService.findAll();

        List<Song> entity = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id) throws Exception {
        final Song entity = elasticsearchService.findById(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
