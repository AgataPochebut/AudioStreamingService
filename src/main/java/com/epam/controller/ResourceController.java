package com.epam.controller;

import com.epam.dto.response.ResourceResponseDto;
import com.epam.dto.response.SongResponseDto;
import com.epam.model.Resource;
import com.epam.service.repository.ResourceService;
import com.epam.service.storage.StorageService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private String realPathtoUploads;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ResourceService service;

    @Autowired
    private StorageService storageService;

    @Autowired
    private Mapper mapper;

    @GetMapping(value = "/downloadFile/{id}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable Long id) throws IOException {

        Resource entity = service.findById(id);

        File file = storageService.downloadFile(entity.getPath());

        return new ResponseEntity<>(new FileSystemResource(file), HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<Resource> uploadFile(@RequestParam("data") MultipartFile file) throws Exception {

        File newfile = storageService.uploadFile(file);

        Resource entity = new Resource();
        entity.setSize(newfile.length());
        entity.setPath(newfile.getPath());
        service.save(entity);

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

//    @PostMapping("/uploadMultipleFiles")
//    public List<Resource> uploadMultipleFiles(@RequestParam("data") MultipartFile[] files) {
////        return Arrays.asList(files)
////                .stream()
////                .map(file -> uploadFile(file))
////                .collect(Collectors.toList());
//        return null;
//    }

}
