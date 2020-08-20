package com.epam.songservice.service.storage.resource;

import com.epam.songservice.configuration.S3Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

@SpringBootTest
@ContextConfiguration(classes = S3Configuration.class)
class ResourceStorageServiceS3Test {

    @Autowired
    private ResourceStorageServiceS3 resourceStorageServiceS3;

    @Test
    void upload() throws IOException {
        Resource resource = null;
        String name = "test";
        resourceStorageServiceS3.upload(resource, name);

    }

    @Test
    void download() {
    }

    @Test
    void delete() {
    }

    @Test
    void exist() {
    }
}