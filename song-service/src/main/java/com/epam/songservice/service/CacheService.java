//package com.epam.songservice.service;
//
//import com.epam.songservice.model.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class CacheService {
//
//    @Cacheable(cacheNames = "myControlledCache", key = "#relevant.id")
//    public org.springframework.core.io.Resource download(Resource relevant) {
//        log.info("Returning NOT from cache!");
//        return new ByteArrayResource("test".getBytes());
//    }
//
//    @CacheEvict(cacheNames = "myControlledCache", key = "#relevant.id")
//    public void delete(Resource relevant) {
//    }
//
////    @CachePut(cacheNames = "myControlledCache", key = "'myControlledPrefix_'.concat(#relevant)")
////    public String upload(String relevant) {
////        return "this is it again!";
////    }
//}
