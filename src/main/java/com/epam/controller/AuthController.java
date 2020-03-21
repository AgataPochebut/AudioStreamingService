package com.epam.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

//import com.epam.authservice.dto.request.UserRequestDto;
//import com.epam.authservice.dto.response.UserResponseDto;
//import org.dozer.Mapper;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping(value = "/login")
    public void login(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "http://localhost:8080/oauth2/authorization/google");
        httpServletResponse.setStatus(302);
    }

    @GetMapping(value = "/logout")
    public void logout(HttpServletResponse httpServletResponse) {
//        httpServletResponse.setHeader("Location", "http://localhost:8080/oauth2/authorization/google");
//        httpServletResponse.setStatus(302);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
