package com.epam.searchservice.controller;

import com.epam.searchservice.model.Album;
import com.epam.searchservice.service.AlbumRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumRepositoryService repositoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void create(@RequestBody Album entity) throws Exception {
        repositoryService.save(entity);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws IOException {
        repositoryService.deleteById(id);
    }
}
