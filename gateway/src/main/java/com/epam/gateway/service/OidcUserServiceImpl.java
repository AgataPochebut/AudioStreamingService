package com.epam.gateway.service;

import com.epam.gateway.feign.AuthServiceClient;
import com.epam.gateway.model.Role;
import com.epam.gateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OidcUserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private AuthServiceClient authServiceClient;

    //    @SneakyThrows
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OidcUserRequest, OidcUser> userService = new OidcUserService();
        OidcUser user = userService.loadUser(userRequest);

        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String token = accessToken.getTokenType().getValue() + ' ' + accessToken.getTokenValue();

        User entity = authServiceClient.getUserByAccount(token, user.getEmail()).getBody();
        if (entity == null) {
            entity = User.builder()
                    .account(user.getEmail())
                    .roles(user.getAuthorities().stream()
                            .map((i) -> Role.builder().name(((GrantedAuthority) i).getAuthority()).build())
                            .collect(Collectors.toSet()))
                    .name(user.getFullName())
                    .build();
            authServiceClient.createUser(token, entity);
        } else {
            entity.setName(user.getFullName());
            authServiceClient.updateUser(token, entity.getId(), entity);
        }

        return user;
    }
}
