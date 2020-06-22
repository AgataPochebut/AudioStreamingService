package com.epam.songservice;

import com.epam.songservice.dto.response.SongResponseDto;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
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
    private SongRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {
        log.info("1234567890");

        model.addAttribute("username", "user");

        final List<Song> entity = repositoryService.findAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());

        model.addAttribute("songs", responseDto);

        return "index";
    }
}
