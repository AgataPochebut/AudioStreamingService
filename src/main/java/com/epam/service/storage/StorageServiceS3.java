package com.epam.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.epam.model.Resource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service("StorageServiceS3")
public class StorageServiceS3 implements StorageService{

//    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${s3.bucketName}")
    private String defaultBucketName;

    @Value("${s3.tempFolder}")
    String defaultBaseFolder;

    public Resource upload(MultipartFile multipartFile) throws IOException {
        File dir = new File(defaultBaseFolder);
        if (!dir.exists()) dir.mkdir();
        File file = new File(defaultBaseFolder, multipartFile.getOriginalFilename());
        if (!file.exists()) file.createNewFile();
        multipartFile.transferTo(file);

        amazonS3Client.putObject(defaultBucketName, multipartFile.getOriginalFilename(), file);
        return Resource.builder()
                .path(amazonS3Client.getUrl(defaultBucketName, file.getName()).toString())
                .parent(defaultBucketName)
                .name(file.getName())
                .build();
    }

    public org.springframework.core.io.Resource download(Resource resource) {
        S3Object s3object = amazonS3Client.getObject(defaultBucketName, resource.getName());
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        return new InputStreamResource(inputStream);
    }

    @Override
    public void delete(Resource resource) {
        amazonS3Client.deleteObject(defaultBucketName, resource.getName());
    }
}
