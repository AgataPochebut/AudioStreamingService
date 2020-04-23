package com.epam.songservice.controller;

import com.epam.songservice.dto.response.SongResponseDto;
import com.epam.songservice.feign.index.SongIndexClient;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.ResourceStorageFactory;
import org.apache.commons.io.FilenameUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private ResourceStorageFactory storageServiceFactory;

    @Autowired
    private SongIndexClient indexClient;

    @Autowired
    private Mapper mapper;

    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SongResponseDto>> read() throws Exception {
        final List<Song> entity = repositoryService.findAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")//, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id) throws Exception {
        final Song entity = repositoryService.findById(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws Exception {
        Song entity = repositoryService.findById(id);
        Resource resource = entity.getResource();
        org.springframework.core.io.Resource source = storageServiceFactory.getService().download(resource);

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(resource.getName())
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(source, headers, HttpStatus.OK);
    }


    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Song entity = upload(multipartFile.getResource());

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<SongResponseDto>> uploadFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        return CompletableFuture.supplyAsync(()-> {
            try {
                return upload(multipartFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/zip", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<SongResponseDto>> uploadZip(@RequestParam("data") MultipartFile multipartFile) throws Exception {

        final List<Song> entity = new ArrayList<>();

        try (ZipInputStream zin = new ZipInputStream(multipartFile.getResource().getInputStream())) {
            ZipEntry entry;
            String name;
            int c;
            while ((entry = zin.getNextEntry()) != null) {

                name = entry.getName(); // получим название файла

                if (!entry.isDirectory() && FilenameUtils.getExtension(name).equals("mp3")) {
                    File file = new File("/temp", FilenameUtils.getName(name));
                    file.getParentFile().mkdirs();
                    try (FileOutputStream fout = new FileOutputStream(file)) {
                        while ((c = zin.read()) >= 0) {
                            fout.write(c);
                        }
                    }

                    entity.add(upload(new FileSystemResource(file)));
                }
                zin.closeEntry();
            }
        }

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/zip/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<List<SongResponseDto>>> uploadZipFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        return CompletableFuture.supplyAsync(()-> {
            try {
                return uploadZip(multipartFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        Song entity = repositoryService.findById(id);
        Resource resource = entity.getResource();

        indexClient.delete(id);

        repositoryService.deleteById(id);

        storageServiceFactory.getService().delete(resource);
    }

    private Song upload(org.springframework.core.io.Resource source) throws Exception {
        Resource resource = storageServiceFactory.getService().upload(source);

        Song entity = Song.builder()
                .resource(resource)
                .build();

        entity = repositoryService.save(entity);

        indexClient.create(entity);

        return entity;
    }
}
