package com.it.songservice.controller;

import com.it.songservice.dto.request.ArtistGetRequestDto;
import com.it.songservice.dto.request.ArtistRequestDto;
import com.it.songservice.dto.response.ArtistResponseDto;
import com.it.songservice.model.Artist;
import com.it.songservice.service.repository.ArtistRepositoryService;
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
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<ArtistResponseDto>> get(@Valid ArtistGetRequestDto requestDto) {
        final List<Artist> list = repositoryService.findAll(requestDto);
        final List<ArtistResponseDto> responseDto = list.stream()
                .map((i) -> mapper.map(i, ArtistResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ArtistResponseDto> get(@PathVariable Long id) {
        Artist entity = repositoryService.findById(id);
        final ArtistResponseDto responseDto = mapper.map(entity, ArtistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody ArtistRequestDto requestDto) throws Exception {
        Artist entity = mapper.map(requestDto, Artist.class);
        entity = repositoryService.save(entity);
        return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@PathVariable Long id, @Valid @RequestBody ArtistRequestDto requestDto) throws Exception {
        Artist entity = mapper.map(requestDto, Artist.class);
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
