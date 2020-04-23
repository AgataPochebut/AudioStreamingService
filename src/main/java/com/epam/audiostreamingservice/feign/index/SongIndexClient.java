package com.epam.audiostreamingservice.feign.index;

import com.epam.audiostreamingservice.model.Song;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "index", url = "http://localhost:8080/index/songs")
public interface SongIndexClient extends GenericIndexClient<Song, Long> {
}