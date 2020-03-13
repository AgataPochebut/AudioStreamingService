//package com.epam.authservice.component;
//
//import mx.wedevelop.oauth2.service.security.UserDetailsImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//
///**
// * Created by colorado on 9/03/17.
// */
//public class GoogleOauth2AuthProvider implements AuthenticationProvider {
//
//    private static final Logger logger = LoggerFactory.getLogger(GoogleOauth2AuthProvider.class);
//
//    @Autowired(required = true)
//    private OAuth2UserService oAuth2UserService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        logger.info("Provider Manager Executed");
//        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//        OAuth2User registeredUser = (OAuth2User) token.getPrincipal();
//        try {
//            OAuth2User registeredUser = userDetailsService
//                    .loadUserByUsername(registeredUser.getEmail());
//        } catch (UsernameNotFoundException usernameNotFoundException) {
//            logger.info("User trying google/login not already a registered user. Register Him !!");
//        }
//        return token;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return OAuth2AuthenticationToken.class
//                .isAssignableFrom(authentication);
//    }
//}
