package com.epam.playservice.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        if(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION)) return;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2Authentication) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();

            if (details.getTokenValue() != null) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, details.getTokenType() + ' ' + details.getTokenValue());
            }
        }
    }
}
