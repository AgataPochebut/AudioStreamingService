package com.epam.gateway;

import com.epam.gateway.feign.AuthServiceClient;
import com.epam.gateway.feign.SongServiceClient;
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

    @Autowired
    private SongServiceClient songServiceClient;

    @GetMapping("/testAuth")
    public void testAuth() throws IOException {
        String test = authServiceClient.testUser();
        System.out.println(test);
    }

    @GetMapping("/testSong")
    public void testSong() throws IOException {
        String test = songServiceClient.testUser();
        System.out.println(test);
    }

}
