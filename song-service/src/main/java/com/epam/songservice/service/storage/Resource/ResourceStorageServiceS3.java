//package com.epam.songservice.service.storage.Resource;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.epam.songservice.annotation.Decorate;
//import com.epam.songservice.annotation.StorageType;
//import com.epam.songservice.model.Resource;
//import com.epam.songservice.model.S3Resource;
//import com.epam.songservice.model.StorageTypes;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Decorate(ResourceStorageDecorator.class)
//@StorageType(StorageTypes.S3)
//@Service
//public class ResourceStorageServiceS3 implements ResourceStorageService {
//
//    @Autowired
//    private AmazonS3 amazonS3Client;
//
//    @Value("${s3.bucketName}")
//    private String defaultBucketName;
//
//    @Value("${s3.defaultFolder}")
//    String defaultBaseFolder;
//
//    public Resource upload(org.springframework.core.io.Resource source, String name) throws IOException {
////        //убрать это
////        File file = new File(defaultBaseFolder, name);
////        file.getParentFile().mkdirs();
////        FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));
////        amazonS3Client.putObject(defaultBucketName, name, file);
//
//        ObjectMetadata meta = new ObjectMetadata();
//        meta.setContentLength(IOUtils.toByteArray(source.getInputStream()).length);
//        meta.setContentMD5(DigestUtils.md5Hex(source.getInputStream()));
//        amazonS3Client.putObject(defaultBucketName, name, source.getInputStream(), meta);
//
//        S3Resource resource = new S3Resource();
//        resource.setName(name);
//        resource.setSize(meta.getContentLength());
//        resource.setChecksum(meta.getContentMD5());
//        resource.setBucketName(defaultBucketName);
//        resource.setKeyName(name);
//        return resource;
//    }
//
//    public org.springframework.core.io.Resource download(Resource resource) {
//        S3Resource currentResource = (S3Resource)resource;
//        S3Object s3object = amazonS3Client.getObject(currentResource.getBucketName(), currentResource.getKeyName());
//        S3ObjectInputStream inputStream = s3object.getObjectContent();
//        return new InputStreamResource(inputStream);
//    }
//
//    @Override
//    public void delete(Resource resource) {
//        S3Resource currentResource = (S3Resource)resource;
//        amazonS3Client.deleteObject(currentResource.getBucketName(), currentResource.getKeyName());
//    }
//
//    @Override
//    public boolean exist(Resource resource) {
//        return false;
//    }
//
//    @Override
//    public String test() {
//        return "S3";
//    }
//}
