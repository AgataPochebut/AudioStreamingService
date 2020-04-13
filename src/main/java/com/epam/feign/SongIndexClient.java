package com.epam.feign;

import com.epam.model.Song;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "search", url = "http://localhost:8080/songs")
public interface SongIndexClient extends GenericIndexClient<Song, Long> {
}