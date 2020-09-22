package com.it.conversionservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;

@SpringBootTest
class ConversionServiceTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ConversionService conversionService;

    @Test
    void convert() throws Exception {
        File file = resourceLoader.getResource("classpath:50 Cent GUnit_Ismell.wav").getFile();
//        File file = new File("src/test/resources/50 Cent GUnit_Ismell.wav");
        File new_file = conversionService.convert(file, "mp3");
    }
}