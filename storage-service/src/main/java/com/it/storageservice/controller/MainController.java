package com.it.storageservice.controller;

import com.it.storageservice.jms.Producer;
import com.it.storageservice.model.Resource;
import com.it.storageservice.service.repository.ResourceRepositoryService;
import com.it.storageservice.service.storage.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/songs")
@Slf4j
public class MainController {

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private ResourceStorageServiceManager storageService;

    @Autowired
    private Producer producer;

    @Autowired
    private Mapper mapper;

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/zip", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public void uploadZip(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Resource resource = storageService.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        producer.uploadZip(resource);
    }

}
