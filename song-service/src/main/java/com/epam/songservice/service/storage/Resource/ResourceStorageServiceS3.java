package com.epam.songservice.service.storage.Resource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.S3Resource;
import com.epam.songservice.model.StorageTypes;
import org.apache.commons.codec.digest.DigestUtils;
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
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(IOUtils.toByteArray(source.getInputStream()).length);
        meta.setContentMD5(DigestUtils.md5Hex(source.getInputStream()));
        amazonS3Client.putObject(defaultBucketName, name, source.getInputStream(), meta);

        S3Resource resource = new S3Resource();
        resource.setName(name);
        resource.setSize(meta.getContentLength());
        resource.setChecksum(meta.getContentMD5());
        resource.setBucketName(defaultBucketName);
        return resource;
    }

    public org.springframework.core.io.Resource download(Resource resource) throws IOException {
        S3Resource currentResource = (S3Resource)resource;
        S3Object s3object = amazonS3Client.getObject(currentResource.getBucketName(), currentResource.getName());
        byte[] content = IOUtils.toByteArray(s3object.getObjectContent());
        return new ByteArrayResource(content);
//        return new InputStreamResource(inputStream);//нельзя перечитывать
    }

    @Override
    public void delete(Resource resource) {
        S3Resource currentResource = (S3Resource)resource;
        amazonS3Client.deleteObject(currentResource.getBucketName(), currentResource.getName());
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
