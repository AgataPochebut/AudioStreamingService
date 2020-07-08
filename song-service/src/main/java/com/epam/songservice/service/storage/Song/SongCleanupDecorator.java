//package com.epam.songservice.service.storage.Song;
//
//import com.epam.songservice.exception.ConversionException;
//import com.epam.songservice.feign.conversion.ConversionClient;
//import com.epam.songservice.jms.Producer;
//import com.epam.songservice.model.Resource;
//import com.epam.songservice.model.Song;
//import com.epam.songservice.service.storage.Resource.ResourceStorageFactory;
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SongCleanupDecorator extends SongStorageDecorator {
//
//    public SongCleanupDecorator(SongStorageService storageService, ConversionClient conversionClient) {
//        super(storageService);
//        this.conversionClient = conversionClient;
//    }
//
//    public SongCleanupDecorator(SongStorageService storageService, ConversionClient conversionClient, ResourceStorageFactory resourceStorageFactory) {
//        super(storageService);
//        this.conversionClient = conversionClient;
//        this.resourceStorageFactory = resourceStorageFactory;
//    }
//
//    public SongCleanupDecorator(SongStorageService storageService, Producer producer) {
//        super(storageService);
//        this.producer = producer;
//    }
//
//    @Override
//    public Song upload(Resource resource) throws Exception {
//        List<String> formats = new ArrayList<>();
//        formats.add("wav");
//
//        if (formats.contains(FilenameUtils.getExtension(resource.getName()))) {
//
//            org.springframework.core.io.Resource source = resourceStorageFactory.getService().download(resource);
//            String name = resource.getName();
//
//            //convert
//            MultipartFile multipartFile = new MockMultipartFile(name, name, "multipart/form-data", source.getInputStream());
//            ResponseEntity<org.springframework.core.io.Resource> response = conversionClient.convert(multipartFile, "mp3");
//            if(response.getStatusCode().isError()) {
//                throw new ConversionException("Error in conversion");
//            }
//            org.springframework.core.io.Resource source1 = response.getBody();
//            String name1 = FilenameUtils.removeExtension(name) + "." + "mp3";
//            //save new
//            Resource resource1 = resourceStorageFactory.getService().upload(source1, name1);
//            //rm old
//            resourceStorageFactory.getService().delete(resource);
//
//            return super.upload(resource1);
//        } else {
//            return super.upload(resource);
//        }
//    }
//
//    //это можно использовать когда conv-serv читает-пишет в бд - тогла обмениваемся json
////    @Override
////    public Song upload1(Resource resource) throws Exception {
////        List<String> formats = new ArrayList<>();
////        formats.add("mp3");
////
////        if (!formats.contains(FilenameUtils.getExtension(resource.getName()))) {
//
////            Resource resource1 = producer.convert(resource, "conv");
////            return super.upload1(resource1);
//
////        } else return super.upload1(resource);
////    }
//
//    @Override
//    public String test() {
//        return super.test() + " Conversion";
//    }
//
//}
