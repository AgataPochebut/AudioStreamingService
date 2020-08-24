package com.epam.songservice.service.storage.resource;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.songservice.configuration.S3TestConfiguration;
import com.epam.songservice.model.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {S3TestConfiguration.class, ResourceStorageServiceS3.class})
//установить проперти
class ResourceStorageServiceS3Test {

    @Autowired
    private ResourceStorageServiceS3 storageService;

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${s3.defaultBucket}")
    private String defaultBucketName;

    @Test
    void testAll() throws IOException {
        String name = "test";
        org.springframework.core.io.Resource source = new ByteArrayResource("test_data".getBytes());

        Resource resource = storageService.upload(source, name);
        assertThat(amazonS3Client.doesObjectExist(defaultBucketName, name)).isEqualTo(true);

        assertThat(storageService.exist(resource)).isEqualTo(true);

        assertThat(storageService.download(resource)).isEqualTo(source);

        storageService.delete(resource);
        assertThat(amazonS3Client.doesObjectExist(defaultBucketName, name)).isEqualTo(false);
    }
}