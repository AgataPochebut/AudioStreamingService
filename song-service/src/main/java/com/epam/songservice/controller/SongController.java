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
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/songs")
@Slf4j
public class SongController {

    @Autowired
    private SongStorageService storageService;

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private Producer producer;

    @Autowired
    private Mapper mapper;

    // Accept 'application/octet-stream'
    @GetMapping(value = "/download/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
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

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        log.info("/songs/upload");
        Song entity = storageService.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());

        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private JmsTemplate jmsTemplate;

    // Content type 'multipart/form-data;boundary
    @PostMapping(value = "/upload/zip", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<SongResponseDto>> uploadZip(@RequestParam("data") MultipartFile multipartFile) throws Exception {

        Resource resource = resourceStorageFactory.getService().upload(multipartFile.getResource(), multipartFile.getOriginalFilename());

        Message receiveMessage = jmsTemplate.sendAndReceive("zip", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    Message sendMessage = session.createMessage();
                    sendMessage.setLongProperty("id", resource.getId());

                    sendMessage.setJMSCorrelationID("123");
                    return sendMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });

        List<Song> entity = receiveMessage.getBody(List.class);

        final List<SongResponseDto> responseDto = entity.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
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
    @PostMapping(value = "/upload/zip/future", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<ResponseEntity<List<SongResponseDto>>> uploadZipFuture(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return uploadZip(multipartFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
//
//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/upload/deferred", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public DeferredResult<ResponseEntity<SongResponseDto>> uploadAsync(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//        final DeferredResult<ResponseEntity<SongResponseDto>> result = new DeferredResult<>(null, null);
//
//        ForkJoinPool.commonPool().submit(() -> {
//            try {
//                result.setResult(upload(multipartFile));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        return result;
//    }

//    @GetMapping(value = "/download1/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
//    @ResponseStatus(value = HttpStatus.OK)
//    public void download1(@PathVariable Long id) throws Exception {
//        producer.download(id);
//        //1.отослать id
//        //2.принять ид, положить в очередь файл стримом
//        //1.взять файл из очереди стримом постепенно по мере поступления
//        //1.вернуть response в виде стрима
//    }
//
//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/upload1", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    @ResponseStatus(value = HttpStatus.OK)
//    public void upload1(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//        producer.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
//    }

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


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        Song entity = repositoryService.findById(id);
        storageService.delete(entity);
    }
}
