package com.epam.feign;

import com.epam.model.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "songs", url = "http://localhost:8080/songs")
public interface SongClient {

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Song>> readAll();

    @GetMapping(value = "/{id}")
    public ResponseEntity<Song> read(@PathVariable Long id);

    @PostMapping
    public ResponseEntity<Song> create(@Valid @RequestBody Song entity);

    @PutMapping(value = "/{id}")
    public ResponseEntity<Song> update(@PathVariable Long id, @Valid @RequestBody Song entity);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id);

}