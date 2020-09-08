package com.it.playservice.service;

import com.it.commonservice.model.auth.AuthUser;
import com.it.playservice.feign.auth.AuthServiceClient;
import com.it.playservice.model.User;
import com.it.playservice.service.repository.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private UserRepositoryService userRepositoryService;

    public User get() throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof BearerTokenAuthentication) {
            AuthUser authUser = (AuthUser) authentication.getPrincipal();
            Map<String, Object> attributes = authUser.getAttributes();
            User user = authServiceClient.getLocalUser(attributes.get("email").toString()).getBody();

            User entity = userRepositoryService.findById(user.getId());
            if(entity == null) {
                entity = user;
                entity = userRepositoryService.save(entity);
            }
            return entity;
        }
        return null;
    }

    public void delete(User entity) throws Exception {
        userRepositoryService.delete(entity);
    }
}
