package com.epam.controller;

import com.epam.dto.request.SongRequestDto;
import com.epam.dto.response.SongResponseDto;
import com.epam.model.Resource;
import com.epam.model.Song;
import com.epam.service.repository.SongService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService service;

    @Autowired
    private Mapper mapper;

    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SongResponseDto>> readAll() {
        final List<Song> entity = service.findAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")//, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id) {
        Song entity = service.findById(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping//(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SongResponseDto> create(@RequestBody SongRequestDto requestDto) throws Exception {
        final Song entity = mapper.map(requestDto, Song.class);
        service.save(entity);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public org.springframework.core.io.Resource download(@PathVariable Long id) throws IOException, URISyntaxException {
        Song entity = service.findById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        String requestUrl = "http://localhost:8080/resources/" + entity.getResource().getId();

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.core.io.Resource resource = restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity,org.springframework.core.io.Resource.class).getBody();
        return resource;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile file) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String requestUrl = "http://localhost:8080/resources";

        RestTemplate restTemplate = new RestTemplate();
        Resource resource = restTemplate.postForObject(requestUrl, requestEntity, Resource.class);

        final Song entity = new Song();
        entity.setResource(resource);
        service.save(entity);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        Song entity = service.findById(id);

        String requestUrl = "http://localhost:8080/resources/" + entity.getResource().getId();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(requestUrl, Void.class);

        service.deleteById(id);
    }
}
