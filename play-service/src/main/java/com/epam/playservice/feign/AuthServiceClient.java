package com.epam.playservice.feign;

import com.epam.playservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "auth-service")
public interface AuthServiceClient {

    @GetMapping(value = "users/byAccount")
//    @GetMapping(value = "users", params = {"byAccount"})
    public ResponseEntity<User> getUserByAccount(@RequestParam("account") String account);

}
