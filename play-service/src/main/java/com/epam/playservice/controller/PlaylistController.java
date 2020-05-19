package com.epam.playservice.controller;

import com.epam.playservice.dto.request.PlaylistRequestDto;
import com.epam.playservice.dto.response.PlaylistResponseDto;
import com.epam.playservice.model.Playlist;
import com.epam.playservice.service.PlaylistService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<PlaylistResponseDto>> readAll() {
        final List<Playlist> entity = service.findAll();

        final List<PlaylistResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, PlaylistResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PlaylistResponseDto> read(@PathVariable Long id) {
        Playlist entity = service.findById(id);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlaylistResponseDto> create(@Valid @RequestBody PlaylistRequestDto requestDto) throws Exception {
        final Playlist entity = mapper.map(requestDto, Playlist.class);
        service.save(entity);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PlaylistResponseDto> update(@PathVariable Long id, @Valid @RequestBody PlaylistRequestDto requestDto) throws Exception {
        final Playlist entity = mapper.map(requestDto, Playlist.class);
        service.update(entity);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

}
