package com.epam.songservice;

import com.epam.songservice.jms.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ApplicationContext context;

    @PostMapping("/1")
    public void test(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        ConversionService service = context.getBean(ConversionService.class);
//        Resource resource = service.convert(multipartFile.getResource(), "mp3");
        File file = new File("", multipartFile.getName());
        File newfile = service.convert(file, "mp3");
    }

    @PostMapping("/2")
    public void test(@RequestParam("name") String name) throws IOException {
        ConversionService service = context.getBean(ConversionService.class);
        String result = service.test(name);
    }
}
