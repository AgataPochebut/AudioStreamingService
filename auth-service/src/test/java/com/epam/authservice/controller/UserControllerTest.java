package com.epam.authservice.controller;

import com.epam.authservice.dto.request.UserRequestDto;
import com.epam.authservice.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest//(properties = {"security.basic.enabled=false"})
//@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
@TestExecutionListeners(mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS, listeners = {
        WithSecurityContextTestExecutionListener.class
})
//script
class UserControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", password = "test", authorities = "USER")
    public void getAllShouldReturn403() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", password = "test", authorities = "ADMIN")
    public void getAllShouldReturnOK() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getByAccountShouldReturnOK() throws Exception {
//        when(service.findByAccount("test")).thenReturn(new User());
        this.mockMvc.perform(get("/users/byAccount").queryParam("account", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void createShouldReturnOK() throws Exception {
        final UserRequestDto user = new UserRequestDto();
        user.setAccount("test");
        user.setRoles(Set.of(Role.USER));
        String json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateShouldReturnOK() throws Exception {
        final UserRequestDto user = new UserRequestDto();
        user.setAccount("test");
        user.setRoles(Set.of(Role.USER));
        String json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").pathInfo("/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShouldReturnOK() {
    }
}