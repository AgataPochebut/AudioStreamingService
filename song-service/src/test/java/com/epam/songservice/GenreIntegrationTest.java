package com.epam.songservice;

import com.epam.songservice.dto.request.GenreRequestDto;
import com.epam.songservice.dto.response.GenreResponseDto;
import com.epam.songservice.model.Genre;
import com.epam.songservice.service.repository.GenreRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Sql(scripts = "/insert_genres.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean_genres.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GenreIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @Test
    void findAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andReturn();

        int count1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class).size();
        int count2 = repositoryService.findAll().size();
        assertThat(count1).isEqualTo(count2);
    }

    @Test
    void findById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/genres/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        Genre obj1 = mapper.map(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GenreResponseDto.class), Genre.class);
        Genre obj2 = repositoryService.findById(1L);
        assertThat(obj1).isEqualTo(obj2);
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

        Genre obj1 = mapper.map(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GenreResponseDto.class), Genre.class);
        assertThat(obj1.getName()).isEqualTo(dto.getName());
        Genre obj2 = repositoryService.findByName("test_new");
        assertThat(obj2).isEqualTo(obj1);
    }

    @Test
    void update() throws Exception {
        GenreRequestDto dto = new GenreRequestDto();
        dto.setName("test_new");

        MvcResult mvcResult = this.mockMvc.perform(put("/genres/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        Genre obj1 = mapper.map(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GenreResponseDto.class), Genre.class);
        assertThat(obj1.getName()).isEqualTo(dto.getName());
        Genre obj2 = repositoryService.findById(1L);
        assertThat(obj2).isEqualTo(obj1);
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/genres/{id}", 1L))
                .andExpect(status().isOk());

        Genre obj = repositoryService.findById(1L);
        assertThat(obj).isNull();
    }

}