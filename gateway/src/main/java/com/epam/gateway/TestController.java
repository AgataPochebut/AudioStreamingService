package com.epam.gateway;

import com.epam.gateway.feign.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private AuthServiceClient authServiceClient;

    @GetMapping("/test")
    public String test() throws IOException {
        return "gateway test";
    }

    @GetMapping("/testAuth")
    public void testAuth() throws IOException {
        String test = authServiceClient.testUser();
        System.out.println(test);

        String test2 = authServiceClient.testUser2("vdsd");
        System.out.println(test2);

    }

}
