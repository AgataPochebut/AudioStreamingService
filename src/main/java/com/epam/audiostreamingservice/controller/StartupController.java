package com.epam.audiostreamingservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartupController {

    @GetMapping
    public String index() {
        return "upload";
    }

}
