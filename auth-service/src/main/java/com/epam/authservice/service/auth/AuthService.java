package com.epam.authservice.service.auth;

import com.epam.commonservice.model.auth.AuthUser;

public interface AuthService {
    AuthUser getUser(String s) throws Exception;
}