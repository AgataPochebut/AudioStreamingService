package com.epam.feign.index;

import com.epam.model.Song;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "index", url = "http://localhost:8080/index/songs")
public interface SongIndexClient extends GenericIndexClient<Song, Long> {
}