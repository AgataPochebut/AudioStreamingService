package com.epam.controller;

import com.epam.service.conversion.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> conversion(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) throws Exception {
        org.springframework.core.io.Resource resource = conversionService.convert(multipartFile.getResource(), format);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
