package com.epam.songservice.controller;

import com.epam.songservice.jms.Producer;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.ResourceRepositoryService;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.resource.ResourceStorageFactory;
import com.epam.songservice.service.storage.song.SongStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SongController.class)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongRepositoryService songRepositoryService;

    @MockBean
    private SongStorageService songStorageService;

    @Mock
    @MockBean
    private ResourceRepositoryService resourceRepositoryService;
    @InjectMocks
    @MockBean
    private ResourceStorageFactory resourceStorageFactory;


    @MockBean
    private Producer producer;

    @MockBean
    private Mapper mapper;

    @Test
    void getById() throws Exception {
        when(songRepositoryService.findById(any())).thenReturn(new Song());
        this.mockMvc.perform(get("/songs/{id}", 1L))
                .andExpect(status().isOk());
    }

//    @Test
//    void delete() {
//    }
}