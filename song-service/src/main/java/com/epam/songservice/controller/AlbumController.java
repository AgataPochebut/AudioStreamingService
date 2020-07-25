package com.epam.songservice.controller;

import com.epam.songservice.dto.request.AlbumRequestDto;
import com.epam.songservice.dto.response.AlbumResponseDto;
import com.epam.songservice.model.Album;
import com.epam.songservice.service.repository.AlbumRepositoryService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumRepositoryService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<AlbumResponseDto>> getAll() {
        final List<Album> entity = service.findAll();

        final List<AlbumResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, AlbumResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AlbumResponseDto> get(@PathVariable Long id) {
        Album entity = service.findById(id);

        final AlbumResponseDto responseDto = mapper.map(entity, AlbumResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDto> create(@Valid @RequestBody AlbumRequestDto requestDto) throws Exception {
        Album entity = mapper.map(requestDto, Album.class);
        entity = service.save(entity);

        final AlbumResponseDto responseDto = mapper.map(entity, AlbumResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AlbumResponseDto> update(@PathVariable Long id, @Valid @RequestBody AlbumRequestDto requestDto) throws Exception {
        Album entity = mapper.map(requestDto, Album.class);
        entity.setId(id);
        entity = service.update(entity);

        final AlbumResponseDto responseDto = mapper.map(entity, AlbumResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

}
