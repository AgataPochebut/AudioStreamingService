package com.it.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/token")
    public String getToken() throws IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    ((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId(),
                    authentication.getName());
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            if (accessToken != null) {
                return accessToken.getTokenValue();
            }
        }
        else if (authentication instanceof BearerTokenAuthentication) {
            OAuth2AccessToken accessToken = ((BearerTokenAuthentication)authentication).getToken();
            if (accessToken != null) {
                return accessToken.getTokenValue();
            }
        }
        return null;
    }
}
