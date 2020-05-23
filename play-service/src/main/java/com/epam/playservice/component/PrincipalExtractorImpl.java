package com.epam.playservice.component;

import com.epam.commonservice.model.User;
import com.epam.playservice.feign.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

//@Component
public class PrincipalExtractorImpl implements PrincipalExtractor {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    public Object extractPrincipal(Map<String, Object> map) {

        User entity = authServiceClient.getUserByAccount(map.get("email").toString()).getBody();
        return entity;
    }
}
