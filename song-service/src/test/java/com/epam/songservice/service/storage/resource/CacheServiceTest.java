//package com.epam.songservice.service.storage.resource;
//
//import com.epam.songservice.model.Resource;
//import com.epam.songservice.service.CacheService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cache.CacheManager;
//
//// нужен весь контекст т.к. автобины spring-boot-starter-redis
//@SpringBootTest
//@Slf4j
//class CacheServiceTest {
//
//    @Autowired
//    private CacheService cacheService;
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Test
//    void testAll() throws Exception {
//        Resource resource = new Resource();
//        resource.setId(1L);
//
//        org.springframework.core.io.Resource firstString = cacheService.download(resource);
//        log.info("First: {}", firstString);
//        org.springframework.core.io.Resource secondString = cacheService.download(resource);
//        log.info("Second: {}", secondString);
//    }
//}