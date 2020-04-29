package com.epam.songservice.feign.index;

import com.epam.songservice.model.Song;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "index", url = "http://localhost:8085/index/songs")
public interface SongIndexClient extends GenericIndexClient<Song, Long> {
}