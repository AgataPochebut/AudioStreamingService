package com.epam.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/test")
public class TestController {

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/1", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public long upload1(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        long start = System.currentTimeMillis();

        Resource source = multipartFile.getResource();
        File file = new File("/Java/temp", source.getFilename());
        file.getParentFile().mkdirs();
        FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        return timeConsumedMillis;
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/2", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public long upload2(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        long start = System.currentTimeMillis();

        Resource source = multipartFile.getResource();
        File file = new File("/Java/temp", source.getFilename());
        file.getParentFile().mkdirs();
        FileUtils.copyInputStreamToFile(source.getInputStream(), file);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        return timeConsumedMillis;
    }
}
