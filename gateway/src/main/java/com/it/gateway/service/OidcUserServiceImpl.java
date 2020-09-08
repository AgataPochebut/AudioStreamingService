package com.it.gateway.service;

import com.it.commonservice.model.auth.AuthUser;
import com.it.gateway.feign.auth.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OidcUserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String token = accessToken.getTokenValue();
        AuthUser user = (AuthUser) authServiceClient.getUser(token).getBody();
        return new DefaultOidcUser(user.getAuthorities(), userRequest.getIdToken(), new OidcUserInfo(user.getAttributes()), "name");
    }
}
