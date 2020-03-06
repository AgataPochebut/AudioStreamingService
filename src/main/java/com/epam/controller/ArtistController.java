package com.epam.controller;

import com.epam.dto.request.ArtistRequestDto;
import com.epam.dto.response.ArtistResponseDto;
import com.epam.model.Artist;
import com.epam.service.ArtistService;
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
    private ArtistService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<ArtistResponseDto>> readAll() {
        final List<Artist> entity = service.findAll();

        final List<ArtistResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, ArtistResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ArtistResponseDto> read(@PathVariable Long id) {
        Artist entity = service.findById(id);

        final ArtistResponseDto responseDto = mapper.map(entity, ArtistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ArtistResponseDto> create(@Valid @RequestBody ArtistRequestDto requestDto) throws Exception {
        final Artist entity = mapper.map(requestDto, Artist.class);
        service.save(entity);

        final ArtistResponseDto responseDto = mapper.map(entity, ArtistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ArtistResponseDto> update(@PathVariable Long id, @Valid @RequestBody ArtistRequestDto requestDto) throws Exception {
        final Artist entity = mapper.map(requestDto, Artist.class);
        service.update(entity);

        final ArtistResponseDto responseDto = mapper.map(entity, ArtistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

}
