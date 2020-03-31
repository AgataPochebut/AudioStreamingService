package com.epam.controller;

import com.epam.dto.request.ResourceRequestDto;
import com.epam.dto.response.ResourceResponseDto;
import com.epam.model.Resource;
import com.epam.service.conversion.ConversionService;
import com.epam.service.repository.ResourceRepositoryService;
import com.epam.service.storage.*;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private Mapper mapper;

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private ResourceStorageFactory storageServiceFactory;

    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResourceResponseDto>> readAll() {
        final List<Resource> entity = repositoryService.findAll();

        final List<ResourceResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, ResourceResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")//, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceResponseDto> read(@PathVariable Long id) throws IOException {
        Resource entity = repositoryService.findById(id);

        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

//    @PostMapping//(consumes = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResourceResponseDto> create(@RequestBody ResourceRequestDto requestDto) throws Exception {
//        final Resource entity = mapper.map(requestDto, Resource.class);
//        repositoryService.save(entity);
//
//        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws IOException {
        Resource entity = repositoryService.findById(id);

        org.springframework.core.io.Resource resource = storageServiceFactory.getService().download(entity);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResourceResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Resource entity = storageServiceFactory.getService().upload(multipartFile.getResource());

        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Resource entity = repositoryService.findById(id);
        storageServiceFactory.getService().delete(entity);
    }

    @GetMapping("/test")
    public String test() {
        return storageServiceFactory.getService().test();
    }

}
