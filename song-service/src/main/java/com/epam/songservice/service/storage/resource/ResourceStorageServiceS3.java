package com.epam.songservice.service.storage.resource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.S3Resource;
import com.epam.songservice.model.StorageTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Decorate(ResourceStorageDecorator.class)
@StorageType(StorageTypes.S3)
@Service
public class ResourceStorageServiceS3 implements ResourceStorageService {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${s3.defaultBucket}")
    private String defaultBucketName;

    public Resource upload(org.springframework.core.io.Resource source, String name) throws IOException {
        int count = 0;
        String key = name;
        while (amazonS3Client.doesObjectExist(defaultBucketName, key)){
            count++;
            key = FilenameUtils.removeExtension(name) + " ("+ count + ")" + "." + FilenameUtils.getExtension(name);
        }

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(source.contentLength());
        meta.setContentMD5(DigestUtils.md5Hex(source.getInputStream()));
        amazonS3Client.putObject(defaultBucketName, key, source.getInputStream(), meta);

        S3Resource resource = new S3Resource();
        resource.setName(key);
        resource.setSize(meta.getContentLength());
        resource.setChecksum(meta.getContentMD5());
        resource.setBucketName(defaultBucketName);
        return resource;
    }

    public org.springframework.core.io.Resource download(Resource resource) throws IOException {
        S3Resource resource1 = (S3Resource)resource;
        S3Object s3object = amazonS3Client.getObject(resource1.getBucketName(), resource1.getName());
        byte[] content = IOUtils.toByteArray(s3object.getObjectContent());
        return new ByteArrayResource(content);
    }

    @Override
    public void delete(Resource resource) {
        S3Resource resource1 = (S3Resource)resource;
        amazonS3Client.deleteObject(resource1.getBucketName(), resource1.getName());
    }

    @Override
    public boolean exist(Resource resource) {
        S3Resource resource1 = (S3Resource)resource;
        return amazonS3Client.doesObjectExist(resource1.getBucketName(), resource1.getName());
    }

    @Override
    public String test() {
        return "S3";
    }
}
