package com.it.songservice.feign.index;

import org.springframework.web.bind.annotation.*;

public interface GenericIndexClient<T,U> {

    @PostMapping
    public T save(@RequestBody T entity);

    @PutMapping(value = "/{id}")
    public T update(@PathVariable U id, @RequestBody T entity);

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable U id);

}