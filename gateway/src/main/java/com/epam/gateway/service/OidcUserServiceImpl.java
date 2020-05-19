package com.epam.gateway.service;

import com.epam.gateway.feign.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OidcUserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private AuthServiceClient authServiceClient;

//    @Autowired
//    private Mapper mapper;

//    @SneakyThrows
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OidcUserRequest, OidcUser> userService = new OidcUserService();
        OidcUser user = userService.loadUser(userRequest);

//        User entity = User.builder()
//                .service(userRequest.getClientRegistration().getClientName())
//                .externalId(user.getName())
//                .account(user.getEmail())
//                .name(user.getFullName())
//                .roles(user.getAuthorities().stream()
//                        .map((i) -> Role.builder().name(((GrantedAuthority) i).getAuthority()).build())
//                        .collect(Collectors.toSet()))
//                .build();
//
////        User entity = mapper.map(user, User.class);
//        authServiceClient.createUser(entity);

        return user;
    }
}
