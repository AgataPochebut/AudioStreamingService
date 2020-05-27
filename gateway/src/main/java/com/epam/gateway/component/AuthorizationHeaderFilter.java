package com.epam.gateway.component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
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
public class AuthorizationHeaderFilter extends ZuulFilter {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();

        // Always remove provided authorization header to not pass through any authorization provided from outside.
        ctx.getZuulRequestHeaders().remove(HttpHeaders.AUTHORIZATION);

        // Do provide OAuth2 access token using authorization header,
        // in case there is such token available in current security context.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                            ((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId(),
                            authentication.getName());

            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

            if (accessToken != null) {
                ctx.addZuulRequestHeader(HttpHeaders.AUTHORIZATION, accessToken.getTokenType().getValue() + ' ' + accessToken.getTokenValue());
            }
        }
        else if (authentication instanceof BearerTokenAuthentication) {
            OAuth2AccessToken accessToken = ((BearerTokenAuthentication)authentication).getToken();

            if (accessToken != null) {
                ctx.addZuulRequestHeader(HttpHeaders.AUTHORIZATION, accessToken.getTokenType().getValue() + ' ' + accessToken.getTokenValue());
            }
        }

        return null;
    }

    @Override
    public boolean shouldFilter() {
        // Always execute filter
        return true;
    }

    @Override
    public String filterType() {
        // Execute before dispatching to microservice
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // Priority within filter chain
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
    }
}
