package com.it.songservice.controller;

import com.it.songservice.dto.request.GenreRequestDto;
import com.it.songservice.dto.response.GenreResponseDto;
import com.it.songservice.model.Genre;
import com.it.songservice.service.repository.GenreRepositoryService;
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
    private GenreRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> getAll() {
        final List<Genre> entity = repositoryService.findAll();

        final List<GenreResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, GenreResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GenreResponseDto> get(@PathVariable Long id) {
        Genre entity = repositoryService.findById(id);

        final GenreResponseDto responseDto = mapper.map(entity, GenreResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GenreResponseDto> save(@Valid @RequestBody GenreRequestDto requestDto) throws Exception {
        Genre entity = mapper.map(requestDto, Genre.class);
        entity = repositoryService.save(entity);

        final GenreResponseDto responseDto = mapper.map(entity, GenreResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GenreResponseDto> update(@PathVariable Long id, @Valid @RequestBody GenreRequestDto requestDto) throws Exception {
        Genre entity = mapper.map(requestDto, Genre.class);
        entity.setId(id);
        entity = repositoryService.update(entity);

        final GenreResponseDto responseDto = mapper.map(entity, GenreResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        repositoryService.deleteById(id);
    }

}
