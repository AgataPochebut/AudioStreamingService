package com.epam.playservice.feign.auth;

import com.epam.commonservice.model.auth.AuthUser;
import com.epam.playservice.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@AllArgsConstructor
public class AuthFallback implements AuthServiceClient {

    private Throwable throwable;

    @Override
    public ResponseEntity<AuthUser> getUser(String s) {
        String errorMessage = throwable.getMessage();
        log.error(errorMessage, throwable);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<User> getLocalUser(String s) {
        String errorMessage = throwable.getMessage();
        log.error(errorMessage, throwable);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
