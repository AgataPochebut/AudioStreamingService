//package com.epam.gateway.service;
//
//import com.epam.gateway.feign.AuthServiceClient;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OAuth2UserServiceImpl implements OAuth2UserService {
//
//    @Autowired
//    private AuthServiceClient authServiceClient;
//
////    @Autowired
////    private Mapper mapper;
//
//    @SneakyThrows
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();
//        OAuth2User user = userService.loadUser(userRequest);
//        return user;
//    }
//}
