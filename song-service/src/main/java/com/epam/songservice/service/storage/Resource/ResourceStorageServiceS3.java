package com.epam.songservice.service.storage.Resource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.S3Resource;
import com.epam.songservice.model.StorageTypes;
import com.epam.songservice.service.repository.ResourceRepositoryService;
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

        amazonS3Client.putObject(defaultBucketName, file.getName(), file);

        S3Resource resource = new S3Resource();
        resource.setName(file.getName());
        resource.setSize(file.length());
        resource.setChecksum(DigestUtils.md5Hex(new FileInputStream(file)));
        resource.setBucketName(defaultBucketName);
        resource.setKeyName(file.getName());
        return resource;

//        return Resource.builder()
//                .path(amazonS3Client.getUrl(defaultBucketName, file.getName()).toString())
////                .parent(defaultBucketName)
//                .name(file.getName())
//                .storageType(StorageTypes.S3)
//                .size(file.length())
//                .checksum(DigestUtils.md5Hex(new FileInputStream(file)))
//                .build();
    }

    public org.springframework.core.io.Resource download(Resource resource) {
        S3Resource currentResource = (S3Resource)resource;
        S3Object s3object = amazonS3Client.getObject(currentResource.getBucketName(), currentResource.getKeyName());
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        return new InputStreamResource(inputStream);
    }

    @Override
    public void delete(Resource resource) {
        S3Resource currentResource = (S3Resource)resource;
        amazonS3Client.deleteObject(currentResource.getBucketName(), currentResource.getKeyName());
    }

    @Override
    public boolean exist(Resource resource) {
        return false;
    }

    @Override
    public String test() {
        return "S3";
    }
}
