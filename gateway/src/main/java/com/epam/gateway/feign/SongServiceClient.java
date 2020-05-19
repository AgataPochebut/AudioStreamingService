package com.epam.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "song-service")
public interface SongServiceClient {

    @GetMapping("songs/test")
    public String testUser();

}
