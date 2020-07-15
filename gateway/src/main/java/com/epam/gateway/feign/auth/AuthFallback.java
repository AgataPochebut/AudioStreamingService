package com.epam.gateway.feign.auth;

import com.epam.gateway.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@AllArgsConstructor
public class AuthFallback implements AuthServiceClient {

    private Throwable throwable;

    @Override
    public ResponseEntity<User> getUserByAccount(String token, String account) {
        String errorMessage = throwable.getMessage();
        log.error(errorMessage, throwable);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<User> createUser(String token, User user) {
        String errorMessage = throwable.getMessage();
        log.error(errorMessage, throwable);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<User> updateUser(String token, Long id, User requestDto) {
        String errorMessage = throwable.getMessage();
        log.error(errorMessage, throwable);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
