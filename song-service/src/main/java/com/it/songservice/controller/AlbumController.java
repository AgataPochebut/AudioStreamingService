package com.it.songservice.controller;

import com.it.songservice.dto.request.AlbumGetRequestDto;
import com.it.songservice.dto.request.AlbumRequestDto;
import com.it.songservice.dto.response.AlbumResponseDto;
import com.it.songservice.model.Album;
import com.it.songservice.service.repository.AlbumRepositoryService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<AlbumResponseDto>> get(@Valid AlbumGetRequestDto requestDto) {
        final List<Album> entity = repositoryService.findAll(requestDto);
        final List<AlbumResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, AlbumResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AlbumResponseDto> get(@PathVariable Long id) {
        Album entity = repositoryService.findById(id);
        final AlbumResponseDto responseDto = mapper.map(entity, AlbumResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody AlbumRequestDto requestDto) throws Exception {
        Album entity = mapper.map(requestDto, Album.class);
        entity = repositoryService.save(entity);
        return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@PathVariable Long id, @Valid @RequestBody AlbumRequestDto requestDto) throws Exception {
        Album entity = mapper.map(requestDto, Album.class);
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
