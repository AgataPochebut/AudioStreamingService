package com.it.conversionservice.controller;

import com.it.conversionservice.service.ConversionService;
import com.it.conversionservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/conversion")
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private StorageService storageService;

    // Accept 'application/octet-stream'
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> convert(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) throws Exception {
        Resource source = multipartFile.getResource();
        String name = multipartFile.getOriginalFilename();
        File resource = storageService.upload(source, name);
        File resource1 = conversionService.convert(resource, format);
        Resource source1 = storageService.download(resource1);
        String name1 = resource1.getName();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment")
                        .filename(name1)
                        .build()
                        .toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(source1);
    }

    // Accept 'application/octet-stream'
    @PostMapping(value = "/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<Resource>> convertFuture(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) {
        return CompletableFuture.supplyAsync(()-> {
            try {
                return convert(multipartFile, format);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @GetMapping
    public String test(){return "test";}
}
