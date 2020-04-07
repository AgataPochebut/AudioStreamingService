package com.epam.controller;

import com.epam.dto.response.SongResponseDto;
import com.epam.model.Resource;
import com.epam.model.Song;
import com.epam.service.song.SongServiceImpl;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongServiceImpl songService;

    @Autowired
    private Mapper mapper;

    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SongResponseDto>> readAll() throws Exception {
        final List<Song> entity = songService.getAll();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")//, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SongResponseDto> read(@PathVariable Long id) throws Exception {
        final Song entity = songService.get(id);

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws Exception {
        final Song entity = songService.get(id);
        final Resource resource = entity.getResource();
        final org.springframework.core.io.Resource source = songService.download(id);

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
        final Song entity = songService.upload(multipartFile.getResource());

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/callable", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Callable<ResponseEntity<SongResponseDto>> uploadCallable(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        return () -> {
            upload(multipartFile);
            return ResponseEntity.ok().build();
        };
    }

    @PostMapping(value = "/deferred", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public DeferredResult<ResponseEntity<SongResponseDto>> uploadDeferred(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        DeferredResult<ResponseEntity<SongResponseDto>> result = new DeferredResult<>();

        new Thread(() -> {
            try {
                result.setResult(upload(multipartFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return result;
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
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, params = {"extension=zip"})
    public ResponseEntity<List<SongResponseDto>> uploadZip(@RequestParam("data") MultipartFile multipartFile) throws Exception {

        final List<Song> entity = songService.uploadZip(multipartFile.getResource());

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, params = {"extension=zip","async"})
    public ResponseEntity<List<SongResponseDto>> uploadZipAsync(@RequestParam("data") MultipartFile multipartFile) throws Exception {

        CompletableFuture<List<Song>> futureEntity = songService.uploadZipAsync(multipartFile.getResource());
        List<Song> entity = futureEntity.get();

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        songService.delete(id);
    }

}
