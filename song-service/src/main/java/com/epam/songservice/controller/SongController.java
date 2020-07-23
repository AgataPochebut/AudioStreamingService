package com.epam.songservice.controller;

import com.epam.songservice.dto.response.SongResponseDto;
import com.epam.songservice.jms.Producer;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.Resource.ResourceStorageFactory;
import com.epam.songservice.service.storage.Song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
@Slf4j
public class SongController {

    @Autowired
    private SongStorageService songStorageService;

    @Autowired
    private SongRepositoryService songRepositoryService;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private Producer producer;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<SongResponseDto>> getAll() {
        final List<Song> entity = songRepositoryService.findAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SongResponseDto> get(@PathVariable Long id) {
        Song entity = songRepositoryService.findById(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/download/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws Exception {
        Song entity = songRepositoryService.findById(id);

        Resource resource = songStorageService.download(entity);
        org.springframework.core.io.Resource source = resourceStorageFactory.getService().download(resource);

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(resource.getName())
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(source, headers, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {

        Resource resource = resourceStorageFactory.getService().upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        Song entity = songStorageService.upload(resource);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload/async", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadAsync(@RequestParam("data") MultipartFile multipartFile) throws Exception {

        Resource resource = resourceStorageFactory.getService().upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        producer.upload(resource, "upl");
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<SongResponseDto>> uploadFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return upload(multipartFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload/deferred", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public DeferredResult<ResponseEntity<SongResponseDto>> uploadDeferred(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        final DeferredResult<ResponseEntity<SongResponseDto>> result = new DeferredResult<>(null, null);

        ForkJoinPool.commonPool().submit(() -> {
            try {
                result.setResult(upload(multipartFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return result;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws Exception {
        Song entity = songRepositoryService.findById(id);

        songStorageService.delete(entity);
    }
}
