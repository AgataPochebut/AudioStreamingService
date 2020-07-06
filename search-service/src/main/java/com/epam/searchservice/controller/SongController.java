package com.epam.searchservice.controller;

import com.epam.searchservice.model.Song;
import com.epam.searchservice.service.SongRepositoryService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private Mapper mapper;

    @Autowired
    private SongRepositoryService repositoryService;

    @PostMapping
    @ResponseBody
    public Song create(@RequestBody Song entity) throws Exception {
        return repositoryService.save(entity);
    }

    @PutMapping
    @ResponseBody
    public Song update(@PathVariable Long id, @RequestBody Song entity) throws Exception {
        Song entity1 = repositoryService.findById(id);
        mapper.map(entity, entity1);

        return repositoryService.update(entity1);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws IOException {
        repositoryService.deleteById(id);
    }
}
