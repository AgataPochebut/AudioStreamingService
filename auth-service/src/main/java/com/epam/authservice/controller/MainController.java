package com.epam.authservice.controller;

import com.epam.authservice.service.repository.UserRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private UserRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getName());

        if (authentication instanceof BearerTokenAuthentication) {
            Map<String, Object> attributes = ((BearerTokenAuthentication) authentication).getTokenAttributes();
//            model.addAttribute("attributes", attributes);
        }

        return "index";
    }
}
