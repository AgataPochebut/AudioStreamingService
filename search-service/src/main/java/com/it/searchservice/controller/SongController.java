package com.it.searchservice.controller;

import com.it.searchservice.model.Song;
import com.it.searchservice.service.SongRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongRepositoryService repositoryService;

    @PostMapping
    @ResponseBody
    public Song save(@RequestBody Song entity) throws Exception {
        return repositoryService.save(entity);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public Song update(@PathVariable Long id, @RequestBody Song entity) throws Exception {
        entity.setId(id);
        return repositoryService.update(entity);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws IOException {
        repositoryService.deleteById(id);
    }
}
