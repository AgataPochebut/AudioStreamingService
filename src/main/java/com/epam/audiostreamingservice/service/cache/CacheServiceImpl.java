//package com.epam.streamingservice.service.cache;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.Set;
//
//public class CacheServiceImpl {
//
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    void t(){
//        redisTemplate.convertAndSend("cache", new Object());
//    }
//
//    void get(){
//        Set<Object> set = redisTemplate.boundSetOps(KEY).members();
//        for(Object o:set){
//            System.out.println(o); //EMPTY
//        }
//    }
//
//}
