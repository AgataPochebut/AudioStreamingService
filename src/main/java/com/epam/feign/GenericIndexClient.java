package com.epam.feign;

import com.epam.dto.response.SongResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface GenericIndexClient<T,U> {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    ResponseEntity<List<T>> find();

    @GetMapping(value = "/index/{id}")
    public ResponseEntity<T> find(@PathVariable U id);

    @PostMapping(value = "/index")
    public ResponseEntity<T> save(@Valid @RequestBody T entity);

    @PutMapping(value = "/index/{id}")
    public ResponseEntity<T> update(@PathVariable U id, @Valid @RequestBody T entity);

    @DeleteMapping(value = "/index/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable U id);


    @GetMapping(value = "/search")
    public ResponseEntity<List<SongResponseDto>> search(@RequestParam String query);
}