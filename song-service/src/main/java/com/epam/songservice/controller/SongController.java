package com.epam.songservice.controller;

import com.epam.songservice.dto.response.SongResponseDto;
import com.epam.songservice.jms.Producer;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.Song.SongStorageService;
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
public class SongController {

    @Autowired
    private SongStorageService storageService;

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private Producer producer;

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
    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws Exception {
        Song entity = repositoryService.findById(id);
        org.springframework.core.io.Resource source = storageService.download(entity);

        Resource resource = entity.getResource();

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(resource.getName())
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(source, headers, HttpStatus.OK);
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/download1/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void download1(@PathVariable Long id) throws Exception {
        producer.download(id);
        //1.отослать id
        //2.принять ид, положить в очередь файл стримом
        //1.взять файл из очереди стримом постепенно по мере поступления
        //1.вернуть response в виде стрима
    }

    //думаю нужен только один метод а остальное на фронтенде
//    // Accept 'application/octet-stream'
//    @GetMapping(value = "/stream/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    @ResponseStatus(value = HttpStatus.OK)
//    public void stream(@PathVariable Long id) throws Exception {
//        producer.download(id);
//
//    }


    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Song entity = storageService.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
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
    public DeferredResult<ResponseEntity<SongResponseDto>> uploadAsync(@RequestParam("data") MultipartFile multipartFile) throws Exception {
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

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload1", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public void upload1(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        producer.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
    }

//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/upload/zip", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<List<SongResponseDto>> uploadZip(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//
//        final List<Song> entity = new ArrayList<>();
//
//        try (ZipInputStream zin = new ZipInputStream(multipartFile.getResource().getInputStream())) {
//            ZipEntry entry;
//            String name;
//            int c;
//            while ((entry = zin.getNextEntry()) != null) {
//
//                name = entry.getName(); // получим название файла
//
//                if (!entry.isDirectory() && FilenameUtils.getExtension(name).equals("mp3")) {
//                    File file = new File("/temp", FilenameUtils.getName(name));
//                    file.getParentFile().mkdirs();
//                    try (FileOutputStream fout = new FileOutputStream(file)) {
//                        while ((c = zin.read()) >= 0) {
//                            fout.write(c);
//                        }
//                    }
//
//                    entity.add(upload(new FileSystemResource(file), file.getName()));
//                }
//                zin.closeEntry();
//            }
//        }
//
//        final List<SongResponseDto> responseDto = entity.stream()
//                .map((i) -> mapper.map(i, SongResponseDto.class))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/upload/zip/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public CompletableFuture<ResponseEntity<List<SongResponseDto>>> uploadZipFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                return uploadZip(multipartFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        });
//    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        Song entity = repositoryService.findById(id);
        storageService.delete(entity);
    }
}
