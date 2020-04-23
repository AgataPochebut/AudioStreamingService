package com.epam.conversionservice.controller;

import com.epam.conversionservice.service.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/conversion")
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    // Accept 'application/octet-stream'
    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> convert(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) {
        Resource resource = null;
        try {
            resource = conversionService.convert(multipartFile.getResource(), format);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(resource.getFilename())
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<Resource>> convertFuture(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) throws Exception {
        return CompletableFuture.supplyAsync(()-> convert(multipartFile, format));
    }


}
