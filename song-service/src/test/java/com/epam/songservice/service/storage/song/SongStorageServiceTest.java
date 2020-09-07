package com.epam.songservice.service.storage.song;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.resource.ResourceStorageServiceManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

//проверять бд и менеджер
@SpringBootTest
//@ContextConfiguration(classes = {SongStorageServiceImpl.class})
class SongStorageServiceTest {

    @Autowired
    private SongStorageService storageService;

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @BeforeEach
    void init() {
        stubFor(post(urlPathMatching(("/conversion")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .withHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename("50 Cent GUnit_Ismell.mp3").build().toString())
                        .withBodyFile("50 Cent GUnit_Ismell.mp3")));

        stubFor(post(urlPathMatching(("/songs")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));
    }

    @Test
    void testAll() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/50 Cent GUnit_Ismell.wav");

        Song song = storageService.upload(source, source.getFilename());
        assertThat(storageService.exist(song)).isTrue();

        org.springframework.core.io.Resource source1 = storageService.download(song);
        assertThat(source1).isNotNull();
        assertThat(source1.contentLength()).isEqualTo(song.getResource().getSize());
        assertThat(DigestUtils.md5Hex(source1.getInputStream())).isEqualTo(song.getResource().getChecksum());

        storageService.delete(song);
        assertThat(storageService.exist(song)).isFalse();
    }

    @Test
    void uploadZip() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/Архив ZIP.zip");
        List<Song> list = storageService.uploadZip(source, source.getFilename());
        list.stream()
                .forEach(i -> assertThat(storageService.exist(i)).isTrue());
    }

}