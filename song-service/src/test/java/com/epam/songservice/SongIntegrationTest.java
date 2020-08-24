package com.epam.songservice;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@ContextConfiguration(classes = {S3TestConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Sql(scripts = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SongIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SongRepositoryService repositoryService;

    @Test
    void findById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/songs/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        Song obj1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Song.class);
        Song obj2 = repositoryService.findById(1L);
        assertThat(obj1).isEqualTo(obj2);
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/songs/{id}", 1L))
                .andExpect(status().isOk());

        Song obj = repositoryService.findById(1L);
        assertThat(obj).isNull();
    }

}
