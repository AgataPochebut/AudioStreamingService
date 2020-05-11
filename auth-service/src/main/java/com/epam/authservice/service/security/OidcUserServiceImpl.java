package com.epam.authservice.service.security;

import com.epam.authservice.model.User;
import com.epam.authservice.repository.UserRepository;
import com.epam.authservice.service.repository.UserRepositoryService;
import lombok.SneakyThrows;
import org.dozer.Mapper;
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
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private Mapper mapper;

    @SneakyThrows
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OidcUserRequest, OidcUser> userService = new OidcUserService();
        OidcUser user = userService.loadUser(userRequest);

        User entity = userRepository
                .findByClientAndExternalId(userRequest.getClientRegistration().getClientName(), user.getName())
                .orElse(new User());

        mapper.map(user, entity);
        userRepositoryService.save(entity);

        return user;
    }
}
