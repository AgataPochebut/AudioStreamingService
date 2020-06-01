package com.epam.songservice.feign.index;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "search-service")
public interface IndexClient {

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Object entity);

    @DeleteMapping(value = "/{id}")
    public void delete(@RequestBody Object entity);
}