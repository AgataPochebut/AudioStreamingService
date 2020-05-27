package com.epam.gateway.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        if(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION)) return;

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    ((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId(),
                    authentication.getName());

            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

            if (accessToken != null) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, accessToken.getTokenType().getValue() + ' ' + accessToken.getTokenValue());
            }
        }
        else if (authentication instanceof BearerTokenAuthentication) {
            OAuth2AccessToken accessToken = ((BearerTokenAuthentication)authentication).getToken();

            if (accessToken != null) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, accessToken.getTokenType().getValue() + ' ' + accessToken.getTokenValue());
            }
        }
    }
}
