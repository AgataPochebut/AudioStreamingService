package com.it.conversionservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class TestController {

    @GetMapping("/test")
    public String test() throws IOException {
        return "conversion test";
    }

}
