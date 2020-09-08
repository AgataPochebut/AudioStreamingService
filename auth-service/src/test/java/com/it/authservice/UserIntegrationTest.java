package com.it.authservice;

import com.it.authservice.dto.request.UserRequestDto;
import com.it.authservice.dto.response.UserResponseDto;
import com.it.authservice.model.Role;
import com.it.authservice.model.User;
import com.it.authservice.service.repository.UserRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Sql(scripts = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void findAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        int count1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class).size();
        int count2 = repositoryService.findAll().size();
        assertThat(count1).isEqualTo(count2);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void findById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        User obj1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User obj2 = repositoryService.findById(1L);
        assertThat(obj1).isEqualTo(obj2);
    }

    @Test
    void findByAccount() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users/byAccount")
                .param("account", "test"))
                .andExpect(status().isOk())
                .andReturn();

        User obj1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User obj2 = repositoryService.findByAccount("test");
        assertThat(obj1).isEqualTo(obj2);
    }

    @Test
    void save() throws Exception {
        UserRequestDto obj = new UserRequestDto();
        obj.setAccount("test_new");
        obj.setRoles(Set.of(Role.USER));

        MvcResult mvcResult = this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)))
                .andExpect(status().isOk())
                .andReturn();

        User obj1 = mapper.map(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserResponseDto.class), User.class);
        assertThat(obj1.getAccount()).isEqualTo(obj.getAccount());
        assertThat(obj1.getRoles()).isEqualTo(obj.getRoles());

        User obj2 = repositoryService.findByAccount("test_new");
        assertThat(obj2).isEqualTo(obj1);
    }

    @Test
    void update() throws Exception {
        UserRequestDto obj = new UserRequestDto();
        obj.setAccount("test_new");
        obj.setRoles(Set.of(Role.USER, Role.ADMIN));

        MvcResult mvcResult = this.mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)))
                .andExpect(status().isOk())
                .andReturn();

        User obj1 = mapper.map(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserResponseDto.class), User.class);
        assertThat(obj1.getAccount()).isEqualTo(obj.getAccount());
        assertThat(obj1.getRoles()).isEqualTo(obj.getRoles());

        User obj2 = repositoryService.findById(1L);
        assertThat(obj2).isEqualTo(obj1);
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        assertThat(repositoryService.existById(1L)).isFalse();
    }

}