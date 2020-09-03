//package com.epam.songservice.service.storage.resource;
//
//import com.epam.songservice.model.Resource;
//import com.epam.songservice.service.StorageService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.transaction.TransactionManager;
//
//@SpringBootTest
//@Slf4j
//class StorageServiceTest {
//
//    @Autowired
//    private StorageService storageService;
//
//    @Autowired
//    private TransactionManager transactionManager;
//
//    @Test
//    void test() throws Exception {
//        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/50 Cent GUnit_Ismell.mp3");
//        Resource firstString = storageService.upload(source, source.getFilename());
////        Resource secondString = storageService.upload(source, source.getFilename());
//    }
//}