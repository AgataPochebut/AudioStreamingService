package com.epam.controller;

import com.epam.model.Song;
import com.epam.service.index.SongIndexService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/songs/index")
public class SongIndexController {

    @Autowired
    private SongIndexService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<Song>> readAll() {
        final List<Song> entity = service.findAll();

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Song> read(@PathVariable Long id) {
        Song entity = service.findById(id);

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Song> create(@Valid @RequestBody Song entity) throws Exception {
        service.save(entity);

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Song> update(@PathVariable Long id, @Valid @RequestBody Song entity) throws Exception {
        service.update(entity);

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        final Song entity = service.findById(id);
        service.delete(entity);
    }

}
