package com.it.songservice.controller;

import com.it.songservice.jms.Producer;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/resources")
@Slf4j
public class ResourceController {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Producer producer;

    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public void upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Resource resource = resourceStorageServiceManager.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        producer.upload(resource);
        // exception & del res - handle in producer + throw uplExc - handle in restExcHandler
    }

}
