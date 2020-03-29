package com.epam.controller;

import com.epam.dto.request.ResourceRequestDto;
import com.epam.dto.response.ResourceResponseDto;
import com.epam.model.Resource;
import com.epam.model.StorageTypes;
import com.epam.service.repository.ResourceRepositoryService;
import com.epam.service.storage.*;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private ResourceStorageServiceFactory storageServiceFactory;

//    @Autowired
//    private ResourceStorageService storageService;

//    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<ResourceResponseDto>> readAll() {
//        final List<Resource> entity = service.findAll();
//
//        final List<ResourceResponseDto> responseDto = entity.stream()
//                .map((i) -> mapper.map(i, ResourceResponseDto.class))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/{id}")//, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResourceResponseDto> read(@PathVariable Long id, @RequestParam(value = "storage", required = false) String storage) throws IOException {
//        Resource entity = service.findById(id);
//
//        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @PostMapping//(consumes = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ResourceResponseDto> create(@RequestBody ResourceRequestDto requestDto) throws Exception {
//        final Resource entity = mapper.map(requestDto, Resource.class);
//        service.save(entity);
//
//        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws IOException {
//        org.springframework.core.io.Resource resource = storageService.download(id);

        Resource entity = repositoryService.findById(id);
        org.springframework.core.io.Resource resource = storageServiceFactory.service().download(entity);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResourceResponseDto> upload(@RequestParam("data") MultipartFile file) throws Exception {
        Resource entity = storageServiceFactory.service().upload(file);

        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
//        storageService.delete(id);

        Resource entity = repositoryService.findById(id);
        storageServiceFactory.service().delete(entity);
        repositoryService.deleteById(id);
    }

    @GetMapping
    public String make() {
        return storageServiceFactory.service().make();
    }
}
