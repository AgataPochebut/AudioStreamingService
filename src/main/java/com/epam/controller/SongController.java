package com.epam.controller;

import com.epam.dto.request.SongDataRequestDto;
import com.epam.dto.request.SongRequestDto;
import com.epam.dto.response.ResourceResponseDto;
import com.epam.dto.response.SongResponseDto;
import com.epam.model.Resource;
import com.epam.model.Song;
import com.epam.service.repository.SongService;
import com.epam.service.storage.StorageService;
import org.apache.commons.io.IOUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<SongResponseDto>> readAll() {
        final List<Song> entity = service.findAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id) {
        Song entity = service.findById(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SongResponseDto> create(@RequestBody SongRequestDto requestDto) throws Exception {
        final Song entity = mapper.map(requestDto, Song.class);
        service.save(entity);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(value = "/downloadFile/{id}")
    public org.springframework.core.io.Resource downloadFile(@PathVariable Long id) throws IOException, URISyntaxException {

        String requestUrl = "http://localhost:8080/resources/downloadFile/" + id;

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.core.io.Resource resource = restTemplate.getForObject(requestUrl, org.springframework.core.io.Resource.class);

        return resource;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<SongResponseDto> uploadFile(@RequestParam("data") MultipartFile file) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String requestUrl = "http://localhost:8080/resources/uploadFile";

        RestTemplate restTemplate = new RestTemplate();
        Resource resource = restTemplate.postForObject(requestUrl, requestEntity, Resource.class);

        final Song entity = new Song();
        entity.setResource(resource);
        service.save(entity);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/uploadMultipleFiles")
    public List<SongResponseDto> uploadMultipleFiles(@RequestParam("data") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
        return null;
    }

}
