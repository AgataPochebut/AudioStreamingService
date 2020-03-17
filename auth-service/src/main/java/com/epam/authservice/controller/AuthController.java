package com.epam.authservice.controller;

import com.epam.authservice.service.UserService;
import com.epam.authservice.model.User;
//import com.epam.authservice.dto.request.UserRequestDto;
//import com.epam.authservice.dto.response.UserResponseDto;
//import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
