package com.it.songservice.feign.index;

import com.it.songservice.model.Song;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "search-service/songs")
public interface SongIndexClient extends GenericIndexClient<Song, Long> {
}