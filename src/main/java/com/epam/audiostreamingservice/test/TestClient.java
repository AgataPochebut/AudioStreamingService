package com.epam.audiostreamingservice.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "test", url = "http://localhost:8080/test")
public interface TestClient {

    @PostMapping("/index/template/{id}")
    public void test11(@PathVariable String id, @RequestBody Object body);

    @GetMapping("/search/template")
    public void test9();
}