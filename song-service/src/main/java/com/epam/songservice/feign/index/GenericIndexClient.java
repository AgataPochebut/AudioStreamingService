package com.epam.songservice.feign.index;

import org.springframework.web.bind.annotation.*;

public interface GenericIndexClient<T,U> {

    @PostMapping
    public T create(@RequestBody T entity);

    @PutMapping(value = "/{id}")
    public T update(@PathVariable U id, @RequestBody T entity);

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable U id);

}