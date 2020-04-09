package com.epam.controller;

import com.epam.dto.response.SongResponseDto;
import com.epam.model.Resource;
import com.epam.model.Song;
import com.epam.service.search.SongSearchService;
import com.epam.service.song.SongServiceImpl;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongServiceImpl songService;

    @Autowired
    private SongSearchService elasticsearchService;

    @Autowired
    private Mapper mapper;

    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SongResponseDto>> readAll() throws Exception {
        final List<Song> entity = songService.getAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")//, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id) throws Exception {
        final Song entity = songService.get(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws Exception {
        final Song entity = songService.get(id);
        final Resource resource = entity.getResource();
        final org.springframework.core.io.Resource source = songService.download(id);

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(resource.getName())
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(source, headers, HttpStatus.OK);
    }


    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) {
        Song entity = null;
        try {
            entity = songService.upload(multipartFile.getResource());
            entity = elasticsearchService.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<SongResponseDto>> uploadFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        return CompletableFuture.supplyAsync(()-> upload(multipartFile));
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/zip", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<SongResponseDto>> uploadZip(@RequestParam("data") MultipartFile multipartFile) {

        List<Song> entity = null;
        try {
            entity = songService.uploadZip(multipartFile.getResource());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/zip/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<List<SongResponseDto>>> uploadZipFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        return CompletableFuture.supplyAsync(()-> uploadZip(multipartFile));
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        songService.delete(id);
        elasticsearchService.deleteById(id);
    }

}
