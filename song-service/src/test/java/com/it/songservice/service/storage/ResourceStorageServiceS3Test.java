package com.it.songservice.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.it.songservice.configuration.S3TestConfiguration;
import com.it.songservice.model.Resource;
import com.it.songservice.model.S3Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceS3;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.util.InMemoryResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

// ContextConfiguration because without decorators
@SpringBootTest
@ContextConfiguration(classes = {S3TestConfiguration.class, ResourceStorageServiceS3.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ResourceStorageServiceS3Test {

    @Autowired
    @Qualifier("resourceStorageServiceS3")
    private ResourceStorageService storageService;

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${s3.defaultBucket}")
    private String defaultBucketName;

    @BeforeEach
    void before() throws Exception {
        String key = "test.txt";
        org.springframework.core.io.Resource source = new InMemoryResource("test");

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(source.contentLength());
        meta.setContentMD5(DigestUtils.md5Hex(source.getInputStream()));
        amazonS3Client.putObject(defaultBucketName, key, source.getInputStream(), meta);
    }

    @Test
    void upload() throws Exception {
        String name = "test_new.txt";
        org.springframework.core.io.Resource source = new InMemoryResource("test");

        Resource resource = storageService.upload(source, name);
        assertThat(storageService.exist(resource)).isTrue();
        assertThat(resource.getSize()).isEqualTo(source.contentLength());
        assertThat(resource.getChecksum()).isEqualTo(DigestUtils.md5Hex(source.getInputStream()));
    }

    @Test
    void download() throws Exception {
        S3Resource resource = new S3Resource();
        resource.setName("test.txt");
        resource.setBucketName(defaultBucketName);

        org.springframework.core.io.Resource source = storageService.download(resource);
        assertThat(source).isNotNull();
        ObjectMetadata meta = amazonS3Client.getObjectMetadata(defaultBucketName, "test.txt");
        assertThat(source.contentLength()).isEqualTo(meta.getContentLength());
        assertThat(DigestUtils.md5Hex(source.getInputStream())).isEqualTo(meta.getContentMD5());
    }

    @Test
    void delete() throws Exception {
        S3Resource resource = new S3Resource();
        resource.setName("test.txt");
        resource.setBucketName(defaultBucketName);

        storageService.delete(resource);
        assertThat(storageService.exist(resource)).isFalse();
    }
}