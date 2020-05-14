//package com.epam.authservice.controller;
//
////import com.epam.authservice.dto.request.UserRequestDto;
////import com.epam.authservice.dto.response.UserResponseDto;
////import org.dozer.Mapper;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
////    @GetMapping(value = "/login")
////    public void login(HttpServletResponse httpServletResponse) {
////        httpServletResponse.setHeader("Location", "http://localhost:8081/oauth2/authorization/google");
////        httpServletResponse.setStatus(302);
////    }
//
//    @GetMapping(value = "/logout")
//    public void logout(HttpServletResponse httpServletResponse) {
////        httpServletResponse.setHeader("Location", "http://localhost:8081/oauth2/authorization/google");
////        httpServletResponse.setStatus(302);
//        SecurityContextHolder.getContext().setAuthentication(null);
//    }
//}
