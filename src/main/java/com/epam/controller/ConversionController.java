package com.epam.controller;

import com.epam.service.conversion.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/conversion")
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    // Accept 'application/octet-stream'
    // Content type 'multipart/form-data;boundary
    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> conversion(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) throws Exception {
        org.springframework.core.io.Resource resource = conversionService.convert(multipartFile.getResource(), format);

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(resource.getFilename())
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
