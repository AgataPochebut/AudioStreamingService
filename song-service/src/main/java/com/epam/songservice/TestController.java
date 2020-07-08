package com.epam.songservice;

import com.epam.songservice.exception.ConversionException;
import com.epam.songservice.feign.conversion.ConversionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ConversionClient conversionClient;

    @GetMapping("/test")
    public String test() throws IOException {
        return "song test";
    }

    @PostMapping("/test/3")
    public Resource test3(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        org.springframework.core.io.Resource source = multipartFile.getResource();
        String name = multipartFile.getOriginalFilename();

        //convert
        MultipartFile multipartFile1 = new MockMultipartFile(name, name, "multipart/form-data", source.getInputStream());
        ResponseEntity<org.springframework.core.io.Resource> response = conversionClient.convert(multipartFile1, "mp3");
        if(response.getStatusCode().isError()) {
            throw new ConversionException("Error in onversion");
        }

        org.springframework.core.io.Resource source1 = response.getBody();
        return source1;
    }
}
