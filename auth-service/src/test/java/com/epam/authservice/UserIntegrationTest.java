package com.epam.authservice;

import com.epam.authservice.model.Role;
import com.epam.authservice.model.User;
import com.epam.authservice.service.repository.UserRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Sql(scripts = "/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryService repositoryService;

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

        User user1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User user2 = repositoryService.findById(1L);
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void findByAccount() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users/byAccount")
                .param("account", "test"))
                .andExpect(status().isOk())
                .andReturn();

        User user1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User user2 = repositoryService.findByAccount("test");
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void save() throws Exception {
        User user = new User();
        user.setAccount("test_new");
        user.setRoles(Set.of(Role.USER));

        MvcResult mvcResult = this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        User user1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertThat(user1.getAccount()).isEqualTo(user.getAccount());
        assertThat(user1.getRoles()).isEqualTo(user.getRoles());

        User user2 = repositoryService.findByAccount("test_new");
        assertThat(user2.getAccount()).isEqualTo(user.getAccount());
        assertThat(user2.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    void update() throws Exception {
        User user = new User();
        user.setAccount("test_new");
        user.setRoles(Set.of(Role.USER, Role.ADMIN));

        MvcResult mvcResult = this.mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        User user1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertThat(user1.getAccount()).isEqualTo(user.getAccount());
        assertThat(user1.getRoles()).isEqualTo(user.getRoles());

        User user2 = repositoryService.findById(1L);
        assertThat(user2.getAccount()).isEqualTo(user.getAccount());
        assertThat(user2.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        User user1 = repositoryService.findById(1L);
        assertThat(user1).isNull();
    }

}