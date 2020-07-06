package com.epam.songservice.feign.resource;

import com.epam.storageservice.model.Resource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "resource-service")
public interface ResourceClient {

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Resource> get(@PathVariable Long id);

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable Long id);

}
