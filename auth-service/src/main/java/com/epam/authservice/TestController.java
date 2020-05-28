package com.epam.authservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class TestController {

    @HystrixCommand(fallbackMethod = "reliable")
    @GetMapping("/test")
    public String test() throws IOException {
        return "auth test";
    }

    public String reliable() {
        return "Cloud Native Java (O'Reilly)";
    }

}
