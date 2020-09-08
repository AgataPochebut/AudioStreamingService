package com.it.playservice.controller;

import com.it.playservice.dto.request.PlaylistRequestDto;
import com.it.playservice.dto.response.PlaylistResponseDto;
import com.it.playservice.model.Playlist;
import com.it.playservice.model.Song;
import com.it.playservice.service.SongService;
import com.it.playservice.service.repository.PlaylistRepositoryService;
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
    private PlaylistRepositoryService playlistRepositoryService;

    @Autowired
    private SongService songService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<PlaylistResponseDto>> getAll() throws Exception {
        final List<Playlist> entity = playlistRepositoryService.findAll();

        final List<PlaylistResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, PlaylistResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PlaylistResponseDto> get(@PathVariable Long id) throws Exception {
        final Playlist entity = playlistRepositoryService.findById(id);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlaylistResponseDto> save(@RequestBody PlaylistRequestDto requestDto) throws Exception {
        Playlist entity = mapper.map(requestDto, Playlist.class);
        entity = playlistRepositoryService.save(entity);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PlaylistResponseDto> update(@PathVariable Long id, @Valid @RequestBody PlaylistRequestDto requestDto) throws Exception {
        Playlist entity = mapper.map(requestDto, Playlist.class);
        entity.setId(id);
        entity = playlistRepositoryService.update(entity);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws Exception {
        playlistRepositoryService.deleteById(id);
    }

    @PutMapping(value = "/{id}/add/{song_id}")
    public ResponseEntity<PlaylistResponseDto> add(@PathVariable Long id, @PathVariable Long song_id) throws Exception {
        Song song = songService.get(song_id);

        Playlist entity = playlistRepositoryService.findById(id);
        entity.getSongs().add(song);
        entity = playlistRepositoryService.update(entity);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/del/{song_id}")
    public ResponseEntity<PlaylistResponseDto> del(@PathVariable Long id, @PathVariable Long song_id) throws Exception {
        Song song = songService.get(song_id);

        Playlist entity = playlistRepositoryService.findById(id);
        entity.getSongs().remove(song);
        entity = playlistRepositoryService.update(entity);

//        if (song.getPlaylists().isEmpty()) songService.delete(song);

        final PlaylistResponseDto responseDto = mapper.map(entity, PlaylistResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/song/{song_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void del1(@PathVariable Long song_id) throws Exception {
        Song song = songService.get(song_id);
        songService.delete(song);
    }
}
