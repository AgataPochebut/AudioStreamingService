package com.it.songservice.service.storage;

import com.it.songservice.model.FSResource;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceFS;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.util.InMemoryResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

// ContextConfiguration because without decorators
@SpringBootTest
@ContextConfiguration(classes = {ResourceStorageServiceFS.class})
class ResourceStorageServiceFSTest {

    @Autowired
    @Qualifier("resourceStorageServiceFS")
    private ResourceStorageService storageService;

    @Value("${fs.defaultFolder}")
    private String defaultBaseFolder;

    @BeforeEach
    void before() throws Exception {
        String key = "test.txt";
        org.springframework.core.io.Resource source = new InMemoryResource("test");

        Files.createDirectories(Paths.get(defaultBaseFolder, key).getParent());
        Files.copy(source.getInputStream(), Paths.get(defaultBaseFolder, key));
    }

    @AfterEach
    void after() throws Exception {
        FileSystemUtils.deleteRecursively(Paths.get(defaultBaseFolder));
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
        FSResource resource = new FSResource();
        resource.setName("test.txt");
        resource.setFolderName(defaultBaseFolder);

        org.springframework.core.io.Resource source = storageService.download(resource);
        assertThat(source).isNotNull();
    }

    @Test
    void delete() throws Exception {
        FSResource resource = new FSResource();
        resource.setName("test.txt");
        resource.setFolderName(defaultBaseFolder);

        storageService.delete(resource);
        assertThat(storageService.exist(resource)).isFalse();
    }
}