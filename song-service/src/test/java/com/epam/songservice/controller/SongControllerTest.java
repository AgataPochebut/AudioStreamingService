package com.epam.songservice.controller;

import com.epam.songservice.configuration.MappingConfiguration;
import com.epam.songservice.jms.Producer;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.resource.ResourceStorageFactory;
import com.epam.songservice.service.storage.song.SongStorageService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SongController.class)
@AutoConfigureMockMvc(addFilters = false) //disable standart spring recurity filters (token)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@Import({MappingConfiguration.class})
class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongRepositoryService repositoryService;

    @MockBean
    private SongStorageService storageService;

    @MockBean
    private ResourceStorageFactory resourceStorageFactory;

    @MockBean
    private Producer producer;

    @Test
    public void getAllShouldReturnOK() throws Exception {
        when(repositoryService.findAll()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/songs"))
                .andExpect(status().isOk());
    }

    @Test
    void getById() throws Exception {
        when(repositoryService.findById(any())).thenReturn(new Song());
        this.mockMvc.perform(get("/songs/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShouldReturnOK() throws Exception {
        doNothing().when(repositoryService).deleteById(any());
        this.mockMvc.perform(delete("/songs/{id}", 1L))
                .andExpect(status().isOk());
    }
}