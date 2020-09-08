package com.it.songservice.controller;

import com.it.songservice.configuration.MappingConfiguration;
import com.it.songservice.dto.request.GenreRequestDto;
import com.it.songservice.model.Genre;
import com.it.songservice.service.repository.GenreRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@Import({MappingConfiguration.class})
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreRepositoryService repositoryService;

    @Test
    public void getAllShouldReturnOK() throws Exception {
        when(repositoryService.findAll()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk());
    }

    @Test
    void getById() throws Exception {
        when(repositoryService.findById(any())).thenReturn(new Genre());
        this.mockMvc.perform(get("/genres/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void saveShouldReturnErrorNameNotNull() throws Exception {
        final GenreRequestDto dto = new GenreRequestDto();
        dto.setName(null);
        when(repositoryService.save(any(Genre.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnErrorNameNotEmpty() throws Exception {
        final GenreRequestDto dto = new GenreRequestDto();
        dto.setName("");
        when(repositoryService.save(any(Genre.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnOK() throws Exception {
        final GenreRequestDto dto = new GenreRequestDto();
        dto.setName("test");
        when(repositoryService.save(any(Genre.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateShouldReturnOK() throws Exception {
        final GenreRequestDto dto = new GenreRequestDto();
        dto.setName("test");
        when(repositoryService.update(any(Genre.class))).then(returnsFirstArg());
        this.mockMvc.perform(put("/genres/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShouldReturnOK() throws Exception {
        doNothing().when(repositoryService).deleteById(any());
        this.mockMvc.perform(delete("/genres/{id}", 1L))
                .andExpect(status().isOk());
    }
}