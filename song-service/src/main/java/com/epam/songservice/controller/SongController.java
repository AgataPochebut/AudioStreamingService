package com.epam.songservice.controller;

import com.epam.songservice.dto.response.SongResponseDto;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
@Slf4j
public class SongController {

    @Autowired
    private SongRepositoryService songRepositoryService;

    @Autowired
    private SongStorageService songStorageService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<SongResponseDto>> getAll() {
        final List<Song> list = songRepositoryService.findAll();
        final List<SongResponseDto> responseDto = list.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws Exception {
        Song entity = songRepositoryService.findById(id);
        org.springframework.core.io.Resource source = songStorageService.download(entity);
        Resource resource = entity.getResource();

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
        Song entity = songStorageService.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/zip", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<SongResponseDto>> uploadZip(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        List<Song> list = songStorageService.uploadZip(multipartFile.getResource(), multipartFile.getOriginalFilename());
        final List<SongResponseDto> responseDto = list.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public CompletableFuture<ResponseEntity<SongResponseDto>> uploadFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                return upload(multipartFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        });
//    }
//
//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/deferred", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public DeferredResult<ResponseEntity<SongResponseDto>> uploadDeferred(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//        final DeferredResult<ResponseEntity<SongResponseDto>> result = new DeferredResult<>(null, null);
//        ForkJoinPool.commonPool().submit(() -> {
//            try {
//                result.setResult(upload(multipartFile));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        return result;
//    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws Exception {
        Song entity = songRepositoryService.findById(id);
        songStorageService.delete(entity);
    }
}
