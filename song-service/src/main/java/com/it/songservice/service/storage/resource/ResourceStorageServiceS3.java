package com.it.songservice.service.storage.resource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.it.songservice.annotation.StorageType;
import com.it.songservice.model.Resource;
import com.it.songservice.model.S3Resource;
import com.it.songservice.model.StorageTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@StorageType(StorageTypes.S3)
@Service("ResourceStorageServiceS3")
public class ResourceStorageServiceS3 implements ResourceStorageService<S3Resource> {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${s3.defaultBucket}")
    private String defaultBucketName;

    public S3Resource upload(org.springframework.core.io.Resource source, String name) throws IOException {
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

    public org.springframework.core.io.Resource download(S3Resource resource) throws IOException {
        S3Object s3object = amazonS3Client.getObject(resource.getBucketName(), resource.getName());
        byte[] content = IOUtils.toByteArray(s3object.getObjectContent());
        return new ByteArrayResource(content);
    }

    @Override
    public void delete(S3Resource resource) {
        amazonS3Client.deleteObject(resource.getBucketName(), resource.getName());
    }

    @Override
    public boolean exist(S3Resource resource) {
        return amazonS3Client.doesObjectExist(resource.getBucketName(), resource.getName());
    }

    @Override
    public boolean supports(Class<? extends Resource> clazz) {
        return S3Resource.class.isAssignableFrom(clazz);
    }
}
