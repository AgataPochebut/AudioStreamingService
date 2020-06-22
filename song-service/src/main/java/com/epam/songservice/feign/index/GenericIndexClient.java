package com.epam.songservice.feign.index;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GenericIndexClient<T,U> {

    @PostMapping
    public T create(@RequestBody T entity);

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable U id);
}