package com.it.songservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.songservice.dto.response.SongResponseDto;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.song.SongStorageService;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

//    @BeforeEach
//    void before() throws Exception {
//        org.springframework.core.io.Resource source_test = new FileSystemResource("src/test/resources/hurts - stay.mp3");
//        storageService.upload(source_test, source_test.getFilename());
//    }
//
//    @Test
//    void upload() throws Exception {
//        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/HURTS - WONDERFUL LIFE.MP3");
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
//        MvcResult mvcResult = this.mockMvc.perform(multipart("/songs")
//                .file(mockMultipartFile)
//                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        SongResponseDto dto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SongResponseDto.class);
//        assertThat(repositoryService.existById(dto.getId())).isTrue();
//
//        Song song = repositoryService.findById(dto.getId());
//        assertThat(storageService.exist(song)).isTrue();
//    }
//
//    @Test
//    void download() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(get("/songs/{id}", 1L)
//                .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        org.springframework.core.io.Resource source = new ByteArrayResource(mvcResult.getResponse().getContentAsByteArray());
//        assertThat(source).isNotNull();
//
//        Song song = repositoryService.findById(1L);
//        assertThat(source.contentLength()).isEqualTo(song.getResource().getSize());
//        assertThat(DigestUtils.md5Hex(source.getInputStream())).isEqualTo(song.getResource().getChecksum());
//    }

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
