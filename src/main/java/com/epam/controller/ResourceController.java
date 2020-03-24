package com.epam.controller;

import com.epam.dto.request.ResourceRequestDto;
import com.epam.dto.request.SongRequestDto;
import com.epam.dto.response.ResourceResponseDto;
import com.epam.dto.response.ResourceResponseDto;
import com.epam.dto.response.SongResponseDto;
import com.epam.model.Resource;
import com.epam.model.Resource;
import com.epam.model.Song;
import com.epam.service.repository.ResourceService;
import com.epam.service.storage.StorageService;
import com.epam.service.storage.StorageServiceFS;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService service;

    @Autowired
    private Mapper mapper;

    @Autowired
    @Qualifier(value = "StorageServiceFS")
    private StorageService storageServiceFS;

    @Autowired
    @Qualifier(value = "StorageServiceS3")
    private StorageService storageServiceS3;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResourceResponseDto>> readAll() {
        final List<Resource> entity = service.findAll();

        final List<ResourceResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, ResourceResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceResponseDto> read(@PathVariable Long id, @RequestParam(value = "storage", required = false) String storage) throws IOException {
        Resource entity = service.findById(id);

        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceResponseDto> create(@RequestBody ResourceRequestDto requestDto) throws Exception {
        final Resource entity = mapper.map(requestDto, Resource.class);
        service.save(entity);

        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id, @RequestParam(value = "storage", required = false) String storage) throws IOException {
        Resource entity = service.findById(id);

        StorageService storageService = (storage=="s3"?storageServiceS3:storageServiceFS);
        org.springframework.core.io.Resource resource = storageService.download(entity);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResourceResponseDto> upload(@RequestParam("data") MultipartFile file, @RequestParam(value = "storage", required = false) String storage) throws Exception {
        StorageService storageService = (storage=="s3"?storageServiceS3:storageServiceFS);
        Resource entity = storageService.upload(file);
        service.save(entity);

        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestParam(value = "storage", required = false) String storage) {
        Resource entity = service.findById(id);

        StorageService storageService = (storage=="s3"?storageServiceS3:storageServiceFS);
        storageService.delete(entity);

        service.deleteById(id);
    }
}
