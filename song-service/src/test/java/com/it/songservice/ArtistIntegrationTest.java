package com.it.songservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.songservice.dto.request.ArtistRequestDto;
import com.it.songservice.dto.response.ArtistResponseDto;
import com.it.songservice.model.Artist;
import com.it.songservice.service.repository.ArtistRepositoryService;
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
@Sql(scripts = "/insert_artists.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean_artists.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ArtistIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ArtistRepositoryService repositoryService;

    @Test
    void getAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andReturn();

        List<ArtistResponseDto> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ArtistResponseDto>>(){});
        assertThat(list.size()).isEqualTo(repositoryService.findAll().size());

        for(ArtistResponseDto dto : list) {
            assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(dto.getId()), ArtistResponseDto.class));
        }
    }

    @Test
    void getById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/artists/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        ArtistResponseDto dto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ArtistResponseDto.class);
        assertThat(dto).isEqualTo(mapper.map(repositoryService.findById(1L), ArtistResponseDto.class));
    }

    @Test
    void save() throws Exception {
        ArtistRequestDto dto = new ArtistRequestDto();
        dto.setName("test_new");

        MvcResult mvcResult = this.mockMvc.perform(post("/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        Artist obj = repositoryService.findById(id);
        assertThat(obj.getName()).isEqualTo(dto.getName());
    }

    @Test
    void update() throws Exception {
        ArtistRequestDto dto = new ArtistRequestDto();
        dto.setName("test_upd");

        MvcResult mvcResult = this.mockMvc.perform(put("/artists/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        Artist obj = repositoryService.findById(1L);
        assertThat(obj.getName()).isEqualTo(dto.getName());
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/artists/{id}", 1L))
                .andExpect(status().isOk());

        assertThat(repositoryService.existById(1L)).isFalse();
    }

}