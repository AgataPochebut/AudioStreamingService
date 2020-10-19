package com.it.authservice.controller;

import com.it.authservice.dto.response.UserResponseDto;
import com.it.authservice.model.User;
import com.it.authservice.service.repository.UserRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private UserRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) throws Exception {

        final List<User> list = repositoryService.findAll();
        final List<UserResponseDto> responseDto = list.stream()
                .map((i) -> mapper.map(i, UserResponseDto.class))
                .collect(Collectors.toList());

        model.addAttribute("users", responseDto);

        return "index";
    }
}
