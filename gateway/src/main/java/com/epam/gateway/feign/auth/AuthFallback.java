package com.epam.gateway.feign.auth;

import com.epam.commonservice.model.auth.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@AllArgsConstructor
public class AuthFallback implements AuthServiceClient {

    private Throwable throwable;

//    @Override
//    public ResponseEntity<Map> getUserInfo(String s) {
//        String errorMessage = throwable.getMessage();
//        log.error(errorMessage, throwable);
//        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    public ResponseEntity<Collection> getAuthorities(String s) {
//        String errorMessage = throwable.getMessage();
//        log.error(errorMessage, throwable);
//        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
//    }

    @Override
    public ResponseEntity<AuthUser> getUser(String s) {
        String errorMessage = throwable.getMessage();
        log.error(errorMessage, throwable);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
