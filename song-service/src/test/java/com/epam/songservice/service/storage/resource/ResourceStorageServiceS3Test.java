package com.epam.songservice.service.storage.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootTest
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