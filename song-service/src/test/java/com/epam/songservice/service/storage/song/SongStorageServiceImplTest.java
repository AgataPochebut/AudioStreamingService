package com.epam.songservice.service.storage.song;

import com.epam.songservice.jms.Producer;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

@SpringBootTest
@Slf4j
class SongStorageServiceImplTest {

    @Autowired
    private SongStorageService songStorageService;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Producer producer;

    @Test
    void uploadZip() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/Архив ZIP.zip");
        List<Song> result = songStorageService.uploadZip(source, source.getFilename());
    }

    @Test
    void download() {
    }

}