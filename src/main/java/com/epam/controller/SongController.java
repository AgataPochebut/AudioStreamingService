package com.epam.controller;

import com.epam.dto.request.SongDataRequestDto;
import com.epam.dto.response.SongResponseDto;
import com.epam.model.Song;
import com.epam.service.repository.SongService;
import org.apache.commons.io.IOUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService service;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<SongResponseDto>> readAll() {
        final List<Song> entity = service.findAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id) {
        Song entity = service.findById(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SongResponseDto> create(@RequestBody SongDataRequestDto requestDto) throws Exception {
        final Song entity = mapper.map(requestDto, Song.class);
        service.save(entity);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //    @Autowired
    //    private FileStorageService fileStorageService;

    @GetMapping(value = "/downloadFile")//, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Resource downloadFile(@RequestParam("url") String url) throws IOException {

//        File file = new File(url);
//        file.toURI();
        Resource resource;

//        resource = new FileSystemResource(url);
        resource = new FileUrlResource(url);

        return resource;

    }

//    @GetMapping(value = "/{id}/downloadFile")//, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseBody
//    public Resource downloadFile(@PathVariable Long id) throws IOException {
//
//        Resource resource = new UrlResource(service.findById(id).getPath());
//        return resource;
//
//    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public SongResponseDto uploadFile(@RequestParam("file") MultipartFile file) {
//        String fileName = fileStorageService.storeFile(file);
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        return new SongResponseDto();//fileName, fileDownloadUri, file.getContentType(), file.getSize());
        return null;
    }

    @PostMapping("/uploadMultipleFiles")
    @ResponseBody
    public List<SongResponseDto> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
        return null;
    }

}
