package com.it.songservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.songservice.dto.request.GenreRequestDto;
import com.it.songservice.dto.response.GenreResponseDto;
import com.it.songservice.model.Genre;
import com.it.songservice.service.repository.GenreRepositoryService;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/insert_genres.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean_genres.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GenreIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Mapper mapper;

    @Autowired
    private GenreRepositoryService repositoryService;

    @Test
    void getAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andReturn();

        List<GenreResponseDto> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<GenreResponseDto>>(){});
        assertThat(list.size()).isEqualTo(repositoryService.findAll().size());

        for(GenreResponseDto dto : list) {
            assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(dto.getId()), GenreResponseDto.class));
        }
    }

    @Test
    void getById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/genres/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        GenreResponseDto dto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GenreResponseDto.class);
        assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(1L), GenreResponseDto.class));
    }

    @Test
    void save() throws Exception {
        GenreRequestDto dto = new GenreRequestDto();
        dto.setName("test_new");

        MvcResult mvcResult = this.mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        GenreResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GenreResponseDto.class);
        Genre obj = repositoryService.findById(responseDto.getId());
        assertThat(obj.getName()).isEqualTo(dto.getName());
    }

    @Test
    void update() throws Exception {
        GenreRequestDto dto = new GenreRequestDto();
        dto.setName("test_upd");

        MvcResult mvcResult = this.mockMvc.perform(put("/genres/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        GenreResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GenreResponseDto.class);
        Genre obj = repositoryService.findById(1L);
        assertThat(obj.getName()).isEqualTo(dto.getName());
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/genres/{id}", 1L))
                .andExpect(status().isOk());

        assertThat(repositoryService.existById(1L)).isFalse();
    }

}