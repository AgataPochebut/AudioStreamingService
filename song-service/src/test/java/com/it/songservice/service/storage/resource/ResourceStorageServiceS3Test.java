package com.it.songservice.service.storage.resource;

import com.it.songservice.configuration.S3TestConfiguration;
import com.it.songservice.model.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.util.InMemoryResource;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

// ContextConfiguration because without decorators
@SpringBootTest
@ContextConfiguration(classes = {S3TestConfiguration.class, ResourceStorageServiceS3.class})
class ResourceStorageServiceS3Test {

    @Autowired
    @Qualifier("ResourceStorageServiceS3")
    private ResourceStorageService storageService;

    @Test
    void testAll() throws Exception {
        org.springframework.core.io.Resource source = new InMemoryResource("test");
        String name = "test.txt";

        Resource resource = storageService.upload(source, name);
        assertThat(storageService.exist(resource)).isTrue();
        assertThat(resource.getSize()).isEqualTo(source.contentLength());
        assertThat(resource.getChecksum()).isEqualTo(DigestUtils.md5Hex(source.getInputStream()));

        org.springframework.core.io.Resource source1 = storageService.download(resource);
        assertThat(DigestUtils.md5Hex(source1.getInputStream())).isEqualTo(DigestUtils.md5Hex(source.getInputStream()));

        storageService.delete(resource);
        assertThat(storageService.exist(resource)).isFalse();
    }
}