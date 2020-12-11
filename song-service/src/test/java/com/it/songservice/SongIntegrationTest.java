package com.it.songservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.songservice.dto.response.SongResponseDto;
import com.it.songservice.model.Song;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
import org.apache.commons.codec.digest.DigestUtils;
import org.dozer.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("storage.type=S3")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SongIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Mapper mapper;

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private SongStorageService storageService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @BeforeEach
    void before() throws Exception {
        Resource source = resourceLoader.getResource("classpath:hurts - stay.mp3");
        com.it.songservice.model.Resource resource = resourceStorageServiceManager.upload(source, source.getFilename());
        storageService.upload(resource);
    }

    @Test
    void upload() throws Exception {
        Resource source = resourceLoader.getResource("classpath:HURTS - WONDERFUL LIFE.MP3");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/songs")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        Song song = repositoryService.findById(id);
        com.it.songservice.model.Resource resource = song.getResource();
        assertThat(resourceStorageServiceManager.exist(resource)).isTrue();
        assertThat(resource.getSize()).isEqualTo(source.contentLength());
        assertThat(resource.getChecksum()).isEqualTo(DigestUtils.md5Hex(source.getInputStream()));
    }

    @Test
    void getAll() throws Exception {
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
    void getById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/songs/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        SongResponseDto dto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SongResponseDto.class);
        assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(1L), SongResponseDto.class));
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/songs/{id}", 1L))
                .andExpect(status().isOk());

        assertThat(repositoryService.existById(1L)).isFalse();
    }

}
