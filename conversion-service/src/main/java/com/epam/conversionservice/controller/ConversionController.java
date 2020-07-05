package com.epam.conversionservice.controller;

import com.epam.conversionservice.service.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/conversion")
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    @Value("${conversion.defaultFolder}")
    private String defaultBaseFolder;

    // Accept 'application/octet-stream'
    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> convert(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) throws IOException {

        Resource source = multipartFile.getResource();
        String name = multipartFile.getOriginalFilename();

        File file = new File(defaultBaseFolder, name);
        file.getParentFile().mkdirs();
        FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));

        File file1 = conversionService.convert(file, format);

        Resource source1 = new FileSystemResource(file1);
        String name1 = file1.getName();

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(name1)
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(source1, headers, HttpStatus.OK);
    }

//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public CompletableFuture<ResponseEntity<Resource>> convertFuture(@RequestParam("data") MultipartFile multipartFile, @RequestParam("format") String format) throws Exception {
//        return CompletableFuture.supplyAsync(()-> convert(multipartFile, format));
//    }
}
