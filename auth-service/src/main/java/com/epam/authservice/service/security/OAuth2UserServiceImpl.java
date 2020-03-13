package com.epam.authservice.service.security;

import com.epam.authservice.model.User;
import com.epam.authservice.service.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService {

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper mapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        oAuth2UserRequest.getClientRegistration().getClientName();

        String email="hfhfhfhff";
        return mapper.map(userService.findByEmail(email), User.class);
    }
}
