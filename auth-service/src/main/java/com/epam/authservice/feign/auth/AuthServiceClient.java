package com.epam.authservice.feign.auth;

import com.epam.commonservice.model.auth.AuthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth-service/auth")//, fallbackFactory = AuthFallbackFactory.class)
public interface AuthServiceClient {

//    @GetMapping(value = "userinfo")
//    ResponseEntity<Map> getUserInfo(@RequestParam("access_token") String s);
//
//    @GetMapping(value = "authorities")
//    ResponseEntity<Collection> getAuthorities(@RequestParam("access_token") String s);

    @GetMapping(value = "user")
    ResponseEntity<AuthUser> getUser(@RequestParam("access_token") String s);

}
