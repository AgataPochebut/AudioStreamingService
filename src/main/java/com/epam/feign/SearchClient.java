package com.epam.feign;

import com.epam.dto.request.SongRequestDto;
import com.epam.dto.response.SongResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "search", url = "http://localhost:8080/search")
public interface SearchClient {

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<SongResponseDto>> readAll();

    @GetMapping(value = "/{id}")
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id);

    @PostMapping
    public ResponseEntity<SongResponseDto> create(@Valid @RequestBody SongRequestDto requestDto);

    @PutMapping(value = "/{id}")
    public ResponseEntity<SongResponseDto> update(@PathVariable Long id, @Valid @RequestBody SongRequestDto requestDto);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id);

    //Search
    @GetMapping(value = "/search")
    public ResponseEntity<List<SongResponseDto>> search(@RequestParam String query);
}