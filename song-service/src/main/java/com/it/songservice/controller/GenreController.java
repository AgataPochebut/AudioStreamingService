package com.it.songservice.controller;

import com.it.songservice.dto.request.GenreGetRequestDto;
import com.it.songservice.dto.request.GenreRequestDto;
import com.it.songservice.dto.response.GenreResponseDto;
import com.it.songservice.model.Genre;
import com.it.songservice.service.repository.GenreRepositoryService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> get(@Valid GenreGetRequestDto requestDto) {
        final List<Genre> entity = repositoryService.findAll(requestDto);
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
    public ResponseEntity<Long> save(@Valid @RequestBody GenreRequestDto requestDto) throws Exception {
        Genre entity = mapper.map(requestDto, Genre.class);
        entity = repositoryService.save(entity);
        return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@PathVariable Long id, @Valid @RequestBody GenreRequestDto requestDto) throws Exception {
        if(!repositoryService.existById(id)) throw new EntityNotFoundException("Entity with this id not exist");
        Genre entity = mapper.map(requestDto, Genre.class);
        entity.setId(id);
        repositoryService.update(entity);
    }

    @DeleteMapping
    public ResponseEntity<Set<Long>> delete(
            @Valid @Size(max = 200) @RequestParam(required = false) String id) {
        Set<Long> list = Arrays.stream(id.split(","))
                .map(s -> {
                    Long i = Long.parseLong(s);
                    try {
                        repositoryService.deleteById(i);
                        return i;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(i -> i != null)
                .collect(Collectors.toSet());
        return ResponseEntity
                .ok()
                .body(list);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        repositoryService.deleteById(id);
    }



}
