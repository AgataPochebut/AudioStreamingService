package com.epam.songservice.service.storage.song;

import com.epam.songservice.model.Song;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@SpringBootTest
class SongStorageServiceImplTest {

    @Autowired
    private SongStorageService songStorageService;

    @Test
    void upload() {

    }

    @Test
    void uploadZip() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/Архив ZIP.zip");
        List<Song> result = songStorageService.uploadZip(source, source.getFilename());
    }


    @Test
    void download1() {
    }
}