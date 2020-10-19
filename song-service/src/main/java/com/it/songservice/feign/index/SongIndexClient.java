package com.it.songservice.feign.index;

import com.it.songservice.model.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "search-service")
public interface SongIndexClient {

    @PostMapping(value = "index")
    public void save(@RequestBody Song entity);

    @PutMapping(value = "index/{id}")
    public void update(@PathVariable Long id, @RequestBody Song entity);

    @DeleteMapping(value = "delete/{id}")
    public void delete(@PathVariable Long id);
}