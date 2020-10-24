package com.it.songservice.controller;

import com.it.songservice.dto.response.SongResponseDto;
import com.it.songservice.jms.Producer;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/songs")
@Slf4j
public class SongController {

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private SongStorageService storageService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Producer producer;

    @GetMapping
    public ResponseEntity<List<SongResponseDto>> getAll() {
        final List<Song> list = repositoryService.findAll();
        final List<SongResponseDto> responseDto = list.stream()
                .map((i) -> mapper.map(i, SongResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void download(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Song entity = repositoryService.findById(id);
        org.springframework.core.io.Resource source = storageService.download(entity);

        if (source == null) {
            log.trace("No matching resource found - returning 404");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // check the resource's media type
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if (RequestMethod.HEAD.equals(request.getMethod())) {
            setHeaders(response, source, mediaType);
            return;
        }

        if (request.getHeader(HttpHeaders.RANGE) != null) {
            writePartialContent(request, response, source, mediaType);
        } else {
            writeContent(response, source, mediaType, entity.getResource().getName());
        }
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SongResponseDto> upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Resource resource = resourceStorageServiceManager.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        Song entity = storageService.upload(resource);
        final SongResponseDto responseDto = mapper.map(entity, SongResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws Exception {
        Song entity = repositoryService.findById(id);
        storageService.delete(entity);
    }

    void writeContent(HttpServletResponse response,
                      org.springframework.core.io.Resource resource, MediaType contentType, String filename) throws IOException {

        setHeaders(response, resource, contentType);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment")
                .filename(filename)
                .build()
                .toString());

        try {
            InputStream in = resource.getInputStream();
            try {
                StreamUtils.copy(in, response.getOutputStream());
            }
            finally {
                try {
                    in.close();
                }
                catch (Throwable ex) {
                    // ignore, see SPR-12999
                }
            }
        }
        catch (FileNotFoundException ex) {
            // ignore, see SPR-12999
        }
    }

    void writePartialContent(HttpServletRequest request, HttpServletResponse response,
                             org.springframework.core.io.Resource resource, MediaType contentType) throws IOException {

        long length = resource.contentLength();

        List<HttpRange> ranges;
        try {
            HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
            ranges = headers.getRange();
        }
        catch (IllegalArgumentException ex) {
            response.addHeader(HttpHeaders.CONTENT_RANGE, "bytes */" + length);
            response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }

        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        if (ranges.size() == 1) {
            HttpRange range = ranges.get(0);

            long start = range.getRangeStart(length);
            long end = range.getRangeEnd(length);
            long rangeLength = end - start + 1;

            setHeaders(response, resource, contentType);
            response.addHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + length);
            response.setContentLength((int) rangeLength);

            InputStream in = resource.getInputStream();
            try {
                copyRange(in, response.getOutputStream(), start, end);
            }
            finally {
                try {
                    in.close();
                }
                catch (IOException ex) {
                    // ignore
                }
            }
        }
        else {
            String boundaryString = MimeTypeUtils.generateMultipartBoundaryString();
            response.setContentType("multipart/byteranges; boundary=" + boundaryString);

            ServletOutputStream out = response.getOutputStream();

            for (HttpRange range : ranges) {
                long start = range.getRangeStart(length);
                long end = range.getRangeEnd(length);

                InputStream in = resource.getInputStream();

                // Writing MIME header.
                out.println();
                out.println("--" + boundaryString);
                if (contentType != null) {
                    out.println("Content-Type: " + contentType);
                }

                out.println("Content-Range: bytes " + start + "-" + end + "/" + length);
                out.println();

                // Printing content
                copyRange(in, out, start, end);
            }
            out.println();
            out.print("--" + boundaryString + "--");
        }
    }

    void setHeaders(HttpServletResponse response, org.springframework.core.io.Resource resource, MediaType mediaType) throws IOException {
        response.setContentLength((int)resource.contentLength());
        response.setContentType(mediaType.toString());
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
    }

    void copyRange(InputStream in, OutputStream out, long start, long end) throws IOException {

        long skipped = in.skip(start);

        if (skipped < start) {
            throw new IOException("Skipped only " + skipped + " bytes out of " + start + " required.");
        }

        long bytesToCopy = end - start + 1;

        byte buffer[] = new byte[StreamUtils.BUFFER_SIZE];
        while (bytesToCopy > 0) {
            int bytesRead = in.read(buffer);
            if (bytesRead <= bytesToCopy) {
                out.write(buffer, 0, bytesRead);
                bytesToCopy -= bytesRead;
            }
            else {
                out.write(buffer, 0, (int) bytesToCopy);
                bytesToCopy = 0;
            }
            if (bytesRead < buffer.length) {
                break;
            }
        }
    }

}
