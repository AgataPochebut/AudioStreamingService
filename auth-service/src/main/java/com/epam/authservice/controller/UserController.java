package com.epam.authservice.controller;

import com.epam.authservice.dto.request.UserRequestDto;
import com.epam.authservice.dto.response.UserResponseDto;
import com.epam.authservice.model.User;
import com.epam.authservice.service.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> readAll() {
        final List<User> entity = service.findAll();

        final List<UserResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, UserResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> read(@PathVariable Long id) {
        User entity = service.findById(id);

        final UserResponseDto responseDto = mapper.map(entity, UserResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto requestDto) throws Exception {
        final User entity = mapper.map(requestDto, User.class);
        service.save(entity);

        final UserResponseDto responseDto = mapper.map(entity, UserResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto requestDto) throws Exception {
        final User entity = mapper.map(requestDto, User.class);
        service.update(entity);

        final UserResponseDto responseDto = mapper.map(entity, UserResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
