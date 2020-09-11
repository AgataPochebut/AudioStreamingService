package com.it.songservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.songservice.dto.response.SongResponseDto;
import com.it.songservice.model.Song;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.song.SongStorageService;
import org.apache.commons.codec.digest.DigestUtils;
import org.dozer.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@DirtiesContext
public class SongIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private SongStorageService storageService;

    @Autowired
    private Mapper mapper;

    @BeforeEach
    void init() throws Exception {
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlPathMatching(("/conversion")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .withHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename("50 Cent GUnit_Ismell.mp3").build().toString())
                        .withBodyFile("50 Cent GUnit_Ismell.mp3")));

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlPathMatching(("/songs")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.delete(urlPathMatching(("/songs")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));
    }

    @Test
    @Sql(scripts = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/songs"))
                .andExpect(status().isOk())
                .andReturn();

        List<SongResponseDto> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SongResponseDto>>(){});
        assertThat(list.size()).isEqualTo(repositoryService.findAll().size());

        for(SongResponseDto dto : list) {
            assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(dto.getId()), SongResponseDto.class));
        }
    }

    @Test
    @Sql(scripts = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/songs/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        SongResponseDto obj1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SongResponseDto.class);
        SongResponseDto obj2 = mapper.map(repositoryService.findById(1L), SongResponseDto.class);
        assertThat(obj1).isEqualTo(obj2);
    }

    @Test
    void download() throws Exception {
        org.springframework.core.io.Resource source_old = new FileSystemResource("src/test/resources/50 Cent GUnit_Ismell.mp3");
        storageService.upload(source_old, source_old.getFilename());

        MvcResult mvcResult = this.mockMvc.perform(get("/songs/{id}", 1L)
                .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        org.springframework.core.io.Resource source = new ByteArrayResource(mvcResult.getResponse().getContentAsByteArray());
        assertThat(source).isNotNull();

        Song song = repositoryService.findById(1L);
        assertThat(source.contentLength()).isEqualTo(song.getResource().getSize());
        assertThat(DigestUtils.md5Hex(source.getInputStream())).isEqualTo(song.getResource().getChecksum());

        storageService.delete(song);
    }

    @Test
    void upload() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/50 Cent GUnit_Ismell.wav");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/songs")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        SongResponseDto dto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SongResponseDto.class);
        assertThat(repositoryService.existById(dto.getId())).isTrue();

        Song song = repositoryService.findById(dto.getId());
        assertThat(storageService.exist(song)).isTrue();

        storageService.delete(song);
    }

    @Test
    void uploadZip() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/Archive.zip");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/songs/zip")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        List<SongResponseDto> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SongResponseDto>>(){});
        for(SongResponseDto dto : list) {
            assertThat(repositoryService.existById(dto.getId())).isTrue();

            Song song = repositoryService.findById(dto.getId());
            assertThat(storageService.exist(song)).isTrue();

            storageService.delete(song);
        }
    }

    @Test
    void deleteById() throws Exception {
        org.springframework.core.io.Resource source_old = new FileSystemResource("src/test/resources/50 Cent GUnit_Ismell.mp3");
        storageService.upload(source_old, source_old.getFilename());

        this.mockMvc.perform(delete("/songs/{id}", 1L))
                .andExpect(status().isOk());

        assertThat(repositoryService.existById(1L)).isFalse();
    }

}
