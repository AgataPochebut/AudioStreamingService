package com.epam.authservice.controller;

import com.epam.authservice.configuration.MappingConfiguration;
import com.epam.authservice.dto.request.UserRequestDto;
import com.epam.authservice.model.Role;
import com.epam.authservice.model.User;
import com.epam.authservice.service.repository.UserRepositoryService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false) //disable standart spring recurity filters (token)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@Import({MappingConfiguration.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepositoryService repositoryService;

    @Test
    @WithMockUser(authorities = "USER")
    public void getAllShouldReturnAuthError403() throws Exception {
        when(repositoryService.findAll()).thenReturn(Arrays.asList());
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllShouldReturnOK() throws Exception {
        when(repositoryService.findAll()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getByIdShouldReturnAuthError403() throws Exception {
        when(repositoryService.findById(any())).thenReturn(new User());
        this.mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getByIdShouldReturnOK() throws Exception {
        when(repositoryService.findById(any())).thenReturn(new User());
        this.mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getByAccountShouldReturnOK() throws Exception {
        when(repositoryService.findByAccount(any())).thenReturn(new User());
        this.mockMvc.perform(get("/users/byAccount")
                .param("account", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void saveShouldReturnErrorAccountNotNull() throws Exception {
        final UserRequestDto dto = new UserRequestDto();
        dto.setAccount(null);
        dto.setRoles(Set.of(Role.USER));
        when(repositoryService.save(any(User.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnErrorAccountNotEmpty() throws Exception {
        final UserRequestDto dto = new UserRequestDto();
        dto.setAccount("");
        dto.setRoles(Set.of(Role.USER));
        when(repositoryService.save(any(User.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnErrorRolesNotNull() throws Exception {
        final UserRequestDto dto = new UserRequestDto();
        dto.setAccount("test");
        dto.setRoles(null);
        when(repositoryService.save(any(User.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnErrorRolesNotEmpty() throws Exception {
        final UserRequestDto dto = new UserRequestDto();
        dto.setAccount("test");
        dto.setRoles(Set.of());
        when(repositoryService.save(any(User.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnOK() throws Exception {
        final UserRequestDto dto = new UserRequestDto();
        dto.setAccount("test");
        dto.setRoles(Set.of(Role.USER));
        when(repositoryService.save(any(User.class))).then(returnsFirstArg());
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateShouldReturnOK() throws Exception {
        final UserRequestDto dto = new UserRequestDto();
        dto.setAccount("test");
        dto.setRoles(Set.of(Role.USER));
        when(repositoryService.update(any(User.class))).then(returnsFirstArg());
        this.mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShouldReturnOK() throws Exception {
        doNothing().when(repositoryService).deleteById(any());
        this.mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }
}