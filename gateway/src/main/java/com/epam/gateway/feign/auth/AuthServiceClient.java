package com.epam.gateway.feign.auth;

import com.epam.gateway.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "auth-service", fallbackFactory = AuthFallbackFactory.class)
public interface AuthServiceClient {

    @GetMapping(value = "users/byAccount")
//    @GetMapping(value = "users", params = {"byAccount"})
    public ResponseEntity<User> getUserByAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("account") String account);

    @PostMapping("users")
    public ResponseEntity<User> createUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody User user);

    @PutMapping("users/{id}")
    public ResponseEntity<User> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id, @RequestBody User user);

//    @GetMapping("roles/{name}")
//    public ResponseEntity<User> getRole(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("name") String name);

//    @PostMapping("roles")
//    public ResponseEntity<User> createRole(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Role role);

}
