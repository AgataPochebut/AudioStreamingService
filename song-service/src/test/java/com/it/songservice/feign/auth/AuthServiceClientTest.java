package com.it.songservice.feign.auth;

import com.it.commonservice.model.auth.AuthUser;
import com.it.commonservice.model.auth.Authority;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthServiceClient client;

    @Test
    void getUserFallback() throws JsonProcessingException {
        ResponseEntity<?> responseEntity = client.getUser("token");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getUser() throws JsonProcessingException {
        AuthUser user = new AuthUser();
        user.setAttributes(Map.of("name", "test"));
        user.setNameAttributeKey("name");
        user.setAuthorities(Set.of(new Authority("USER"), new Authority("ADMIN")));
        stubFor(get(urlPathMatching(("/auth/user")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(user))));
        ResponseEntity<AuthUser> responseEntity = client.getUser("token");
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getBody()).isEqualTo(user);
    }

}