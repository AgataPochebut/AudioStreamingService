package com.epam.songservice.controller;

import com.epam.songservice.dto.request.GenreRequestDto;
import com.epam.songservice.dto.response.GenreResponseDto;
import com.epam.songservice.model.Genre;
import com.epam.songservice.service.repository.GenreRepositoryService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreRepositoryService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> getAll() {
        final List<Genre> entity = service.findAll();

        final List<GenreResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, GenreResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GenreResponseDto> get(@PathVariable Long id) {
        Genre entity = service.findById(id);

        final GenreResponseDto responseDto = mapper.map(entity, GenreResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GenreResponseDto> create(@Valid @RequestBody GenreRequestDto requestDto) throws Exception {
        Genre entity = mapper.map(requestDto, Genre.class);
        entity = service.save(entity);

        final GenreResponseDto responseDto = mapper.map(entity, GenreResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GenreResponseDto> update(@PathVariable Long id, @Valid @RequestBody GenreRequestDto requestDto) throws Exception {
        Genre entity = mapper.map(requestDto, Genre.class);
        entity.setId(id);
        entity = service.update(entity);

        final GenreResponseDto responseDto = mapper.map(entity, GenreResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

}
