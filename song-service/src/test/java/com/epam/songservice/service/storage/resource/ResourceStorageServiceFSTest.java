package com.epam.songservice.service.storage.resource;

import com.epam.songservice.model.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.util.InMemoryResource;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

// ContextConfiguration because without decorators
@SpringBootTest
@ContextConfiguration(classes = {ResourceStorageServiceFS.class})
class ResourceStorageServiceFSTest {

    @Autowired
    private ResourceStorageServiceFS storageService;

    @Value("${fs.defaultFolder}")
    private String defaultBaseFolder;

    @Test
    void testAll() throws IOException {
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