package com.it.authservice.controller;

import com.it.authservice.service.auth.AuthService;
import com.it.commonservice.model.auth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping(value = "/user")
    public ResponseEntity<AuthUser> getUser(@RequestParam("access_token") String s) throws Exception {
        AuthUser user = authService.getUser(s);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
