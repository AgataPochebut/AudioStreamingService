package com.it.songservice.controller;

import com.it.songservice.dto.response.SongResponseDto;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
@Slf4j
public class SongController {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private SongStorageService storageService;

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<SongResponseDto>> get() {
        final List<Song> list = repositoryService.findAll();
        final List<SongResponseDto> responseDto = list.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Accept 'application/json'
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SongResponseDto> get(@PathVariable Long id) {
        Song entity = repositoryService.findById(id);
        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Resource resource = resourceStorageServiceManager.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        Song entity = storageService.upload(resource);
        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Set<Long>> delete(@Valid @Size(max = 200) @RequestParam(required = false) String id) {
        Set<Long> list = Arrays.stream(id.split(","))
                .map(s -> {
                    Long i = Long.parseLong(s);
                    try {
                        Song entity = repositoryService.findById(i);
                        storageService.delete(entity);
                        return i;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(i -> i != null)
                .collect(Collectors.toSet());
        return ResponseEntity
                .ok()
                .body(list);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws Exception {
        Song entity = repositoryService.findById(id);
        storageService.delete(entity);
    }
}
