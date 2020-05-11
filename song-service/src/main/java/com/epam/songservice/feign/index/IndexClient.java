package com.epam.songservice.feign.index;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(value = "index", url = "http://localhost:8085/index/songs")
public interface IndexClient<T,U> {

    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity);

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable U id);
}