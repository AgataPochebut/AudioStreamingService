package com.it.searchservice.controller;

import com.it.searchservice.model.Song;
import com.it.searchservice.service.SongRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongRepositoryService repositoryService;

    @GetMapping
    @ResponseBody
    public List<Song> getAll() throws Exception {
        return repositoryService.findAll();
    }

    @PostMapping(value = "/index")
    @ResponseBody
    public Song save(@RequestBody Song entity) throws Exception {
        return repositoryService.save(entity);
    }

    @PutMapping(value = "/index/{id}")
    @ResponseBody
    public Song update(@PathVariable Long id, @RequestBody Song entity) throws Exception {
        entity.setId(id);
        return repositoryService.update(entity);
    }

    @DeleteMapping(value = "/index/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws IOException {
        repositoryService.deleteById(id);
    }

    @GetMapping(value = "/search")
    @ResponseBody
    public List<Song> search(@RequestParam(required = false) String keyword) throws Exception {
        return repositoryService.search(keyword);
    }
}
