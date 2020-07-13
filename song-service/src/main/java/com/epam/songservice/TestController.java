package com.epam.songservice;

import com.epam.songservice.exception.ConversionException;
import com.epam.songservice.feign.conversion.ConversionClient;
import com.epam.songservice.service.storage.Resource.ResourceStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        if (response.getStatusCode().isError()) {
            throw new ConversionException("Error in onversion");
        }

        org.springframework.core.io.Resource source1 = response.getBody();
        return source1;
    }

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    RedisCacheManager cacheManager;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @PostMapping("/test/4")
    public void test4(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        org.springframework.core.io.Resource source = multipartFile.getResource();
        String name = multipartFile.getOriginalFilename();

        com.epam.songservice.model.Resource resource = resourceStorageFactory.getService().upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        cacheManager.getCache("resources1").put(resource.getId(), source);

        org.springframework.core.io.Resource source1 = (Resource) cacheManager.getCache("resources1").get(resource);
    }

    @PostMapping("/test/5")
    public void test5(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        org.springframework.core.io.Resource source = multipartFile.getResource();
        String name = multipartFile.getOriginalFilename();

        cacheManager.getCache("resources2").put(name, source);

        org.springframework.core.io.Resource source1 = (Resource) cacheManager.getCache("resources2").get(name);
    }

    @PostMapping("/test/6")
    public void test6(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        org.springframework.core.io.Resource source = multipartFile.getResource();
        String name = multipartFile.getOriginalFilename();

        cacheManager.getCache("resources2").put(name, source.getInputStream());

        org.springframework.core.io.Resource source1 = (Resource) cacheManager.getCache("resources2").get(name);
    }

    @PostMapping("/test/7")
    public void test7(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        org.springframework.core.io.Resource source = multipartFile.getResource();

        ZipInputStream zin;
        ZipEntry entry;
        String name;

        zin = new ZipInputStream(source.getInputStream(), Charset.forName("windows-1251"));
        while ((entry = zin.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                name = entry.getName(); // получим название файла
                name = java.net.URLEncoder.encode(entry.getName(), StandardCharsets.UTF_8);
            }
            zin.closeEntry();
        }
        zin.close();
    }
}
