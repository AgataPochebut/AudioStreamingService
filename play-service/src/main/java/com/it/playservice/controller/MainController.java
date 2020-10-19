package com.it.playservice.controller;

import com.it.playservice.dto.response.PlaylistResponseDto;
import com.it.playservice.model.Playlist;
import com.it.playservice.service.repository.PlaylistRepositoryService;
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
    private PlaylistRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) throws Exception {

        final List<Playlist> list = repositoryService.findAll();
        final List<PlaylistResponseDto> responseDto = list.stream()
                .map((i) -> mapper.map(i, PlaylistResponseDto.class))
                .collect(Collectors.toList());

        model.addAttribute("playlists", responseDto);

        return "index";
    }
}
