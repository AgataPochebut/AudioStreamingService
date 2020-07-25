package com.epam.playservice.feign.auth;

import com.epam.commonservice.model.auth.AuthUser;
import com.epam.playservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth-service")//), fallbackFactory = AuthFallbackFactory.class)
public interface AuthServiceClient {

    @GetMapping(value = "auth/user")
    ResponseEntity<AuthUser> getUser(@RequestParam("access_token") String s);

//    @GetMapping(value = "localUser")
//    ResponseEntity<User> getLocalUser(@RequestParam("access_token") String s);

    @GetMapping(value = "users/byAccount")
    ResponseEntity<User> getLocalUser(@RequestParam("account") String s);
}
