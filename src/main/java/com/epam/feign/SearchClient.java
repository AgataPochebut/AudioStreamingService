package com.epam.feign;

import com.epam.dto.response.SongResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "search", url = "http://localhost:8080/search")
public interface SearchClient {

    @GetMapping("/songs")
    public ResponseEntity<List<SongResponseDto>> search(@RequestParam String query);

}