package com.epam.feign.index;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface GenericIndexClient<T,U> {

    @GetMapping
    ResponseEntity<List<T>> read();

    @GetMapping(value = "/{id}")
    public ResponseEntity<T> read(@PathVariable U id);

    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity);

    @PutMapping(value = "/{id}")
    public ResponseEntity<T> update(@PathVariable U id, @Valid @RequestBody T entity);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable U id);
}