package com.it.songservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.songservice.dto.response.AlbumResponseDto;
import com.it.songservice.service.repository.AlbumRepositoryService;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/insert_albums.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean_albums.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AlbumIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlbumRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @Test
    void getAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/albums"))
                .andExpect(status().isOk())
                .andReturn();

        List<AlbumResponseDto> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<AlbumResponseDto>>(){});
        assertThat(list.size()).isEqualTo(repositoryService.findAll().size());

        for(AlbumResponseDto dto : list) {
            assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(dto.getId()), AlbumResponseDto.class));
        }
    }

    @Test
    void getById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/albums/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        AlbumResponseDto dto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AlbumResponseDto.class);
        assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(1L), AlbumResponseDto.class));
    }

//    @Test
//    void save() throws Exception {
//        AlbumRequestDto dto = new AlbumRequestDto();
//        dto.setName("test_new");
//
//        MvcResult mvcResult = this.mockMvc.perform(post("/albums")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        AlbumResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AlbumResponseDto.class);
//        Album obj = repositoryService.findById(responseDto.getId());
//        assertThat(obj.getName()).isEqualTo(dto.getName());
//    }
//
//    @Test
//    void update() throws Exception {
//        AlbumRequestDto dto = new AlbumRequestDto();
//        dto.setName("test_upd");
//
//        MvcResult mvcResult = this.mockMvc.perform(put("/albums/{id}", 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        AlbumResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AlbumResponseDto.class);
//        Album obj = repositoryService.findById(1L);
//        assertThat(obj.getName()).isEqualTo(dto.getName());
//    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/albums/{id}", 1L))
                .andExpect(status().isOk());

        assertThat(repositoryService.existById(1L)).isFalse();
    }

}