package com.epam.songservice.controller;

import com.epam.songservice.dto.request.ArtistRequestDto;
import com.epam.songservice.dto.response.ArtistResponseDto;
import com.epam.songservice.model.Artist;
import com.epam.songservice.service.repository.ArtistRepositoryService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<ArtistResponseDto>> getAll() {
        final List<Artist> entity = repositoryService.findAll();

        final List<ArtistResponseDto> responseDto = entity.stream()
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
    public ResponseEntity<ArtistResponseDto> save(@Valid @RequestBody ArtistRequestDto requestDto) throws Exception {
        Artist entity = mapper.map(requestDto, Artist.class);
        entity = repositoryService.save(entity);

        final ArtistResponseDto responseDto = mapper.map(entity, ArtistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ArtistResponseDto> update(@PathVariable Long id, @Valid @RequestBody ArtistRequestDto requestDto) throws Exception {
        Artist entity = mapper.map(requestDto, Artist.class);
        entity.setId(id);
        entity = repositoryService.update(entity);

        final ArtistResponseDto responseDto = mapper.map(entity, ArtistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        repositoryService.deleteById(id);
    }

}
