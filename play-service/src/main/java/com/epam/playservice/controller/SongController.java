//package com.epam.playservice.controller;
//
//import com.epam.playservice.dto.request.PlaylistRequestDto;
//import com.epam.playservice.dto.response.PlaylistResponseDto;
//import com.epam.playservice.model.Playlist;
//import com.epam.playservice.model.Song;
//import com.epam.playservice.service.repository.PlaylistRepositoryService;
//import org.dozer.Mapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/playlists")
//public class SongController {
//
//    @Autowired
//    private PlaylistRepositoryService service;
//
//    @Autowired
//    private Mapper mapper;
//
//    @PostMapping
//    public ResponseEntity<Song> create(@RequestBody Song requestDto) throws Exception {
//        Song entity = mapper.map(requestDto, Song.class);
//        entity = service.save(entity);
//
//        final Song responseDto = mapper.map(entity, Song.class);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    @ResponseStatus(value = HttpStatus.OK)
//    public void delete(@PathVariable Long id) {
//        service.deleteById(id);
//    }
//
//}
