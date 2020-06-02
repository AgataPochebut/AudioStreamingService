package com.epam.storageservice.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        if(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION)) return;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof BearerTokenAuthentication) {
            OAuth2AccessToken accessToken = ((BearerTokenAuthentication)authentication).getToken();

            if (accessToken != null) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, accessToken.getTokenType().getValue() + ' ' + accessToken.getTokenValue());
            }
        }
    }
}
