package com.epam.songservice.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.epam.songservice.annotation.*;
import com.epam.songservice.model.*;
import com.epam.songservice.service.repository.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Decorate(ResourceStorageDecorator.class)
@StorageType(StorageTypes.S3)
@Service
public class ResourceStorageServiceS3 implements ResourceStorageService {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${s3.bucketName}")
    private String defaultBucketName;

    @Value("${s3.defaultFolder}")
    String defaultBaseFolder;

    @Autowired
    private ResourceRepositoryService repositoryService;

    public Resource upload(org.springframework.core.io.Resource source, String name) throws IOException {
        File dir = new File(defaultBaseFolder);
        if (!dir.exists()) dir.mkdir();
        File file = new File(defaultBaseFolder, source.getFilename());
        if (!file.exists()) file.createNewFile();
        FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));

//        ObjectMetadata md = new ObjectMetadata();
//        md.setContentMD5("foobar");

        amazonS3Client.putObject(defaultBucketName, file.getName(), file);
        return Resource.builder()
                .path(amazonS3Client.getUrl(defaultBucketName, file.getName()).toString())
                .parent(defaultBucketName)
                .name(file.getName())
                .storageType(StorageTypes.S3)
                .size(file.length())
                .checksum(DigestUtils.md5Hex(new FileInputStream(file)))
                .build();
    }

    public org.springframework.core.io.Resource download(Resource resource) {
        S3Object s3object = amazonS3Client.getObject(defaultBucketName, resource.getName());
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        return new InputStreamResource(inputStream);
    }

//    @Override
//    public org.springframework.core.io.Resource download(Long id) {
//        Resource resource = repositoryService.findById(id);
//        return download(resource);
//    }

    @Override
    public void delete(Resource resource) {
        amazonS3Client.deleteObject(defaultBucketName, resource.getName());
    }

//    @Override
//    public void delete(Long id) {
//        Resource resource = repositoryService.findById(id);
//        delete(resource);
//    }

    @Override
    public boolean exist(Resource resource) {
        return false;
    }

//    @Override
//    public boolean exist(Long id) {
//        Resource resource = repositoryService.findById(id);
//        return exist(resource);
//    }

    @Override
    public String test() {
        return "S3";
    }
}
