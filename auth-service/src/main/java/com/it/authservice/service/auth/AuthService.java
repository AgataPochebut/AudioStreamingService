package com.it.authservice.service.auth;

import com.it.commonservice.model.auth.AuthUser;

public interface AuthService {
    AuthUser getUser(String s) throws Exception;
}