package com.it.playservice.feign.song;

import com.it.playservice.model.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "song-service")//), fallbackFactory = AuthFallbackFactory.class)
public interface SongServiceClient {

    @GetMapping(value = "songs/{id}")
    ResponseEntity<Song> getSong(@PathVariable Long id);

}
