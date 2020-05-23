package com.epam.playservice.service;

import com.epam.commonservice.model.User;
import com.epam.playservice.feign.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthUserService {

    @Autowired
    private AuthServiceClient authServiceClient;

    public User getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2Authentication) {
            Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();

            if(userAuthentication instanceof UsernamePasswordAuthenticationToken){
                Map<String, Object> details = (Map<String, Object>) userAuthentication.getDetails();
                return authServiceClient.getUserByAccount(details.get("email").toString()).getBody();
            }
        }
        return null;
    }
}
