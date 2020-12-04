package com.it.songservice.controller;

import com.it.songservice.dto.response.ResourceResponseDto;
import com.it.songservice.dto.response.UploadResultResponseDto;
import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadResult;
import com.it.songservice.service.repository.ResourceRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.upload.ResourceUploadService;
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


@RestController
@RequestMapping("/resources")
@Slf4j
public class ResourceController {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    ResourceUploadService resourceUploadService;

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private Mapper mapper;

    @GetMapping(value = "uploadResult/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UploadResultResponseDto uploadResult(@PathVariable Long id) throws Exception {
        UploadResult uploadResult = resourceUploadService.getResultById(id);
        final UploadResultResponseDto responseDto = mapper.map(uploadResult, UploadResultResponseDto.class);
        return responseDto;
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceResponseDto> get(@PathVariable Long id) {
        Resource entity = repositoryService.findById(id);
        final ResourceResponseDto responseDto = mapper.map(entity, ResourceResponseDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Content type 'multipart/form-data;boundary
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Long upload(@RequestParam("data") MultipartFile multipartFile) throws Exception {
        Resource resource = resourceStorageServiceManager.upload(multipartFile.getResource(), multipartFile.getOriginalFilename());
        resourceUploadService.upload(resource);
        return resource.getId();
    }

    // Accept 'application/octet-stream'
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void download(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Resource resource = repositoryService.findById(id);
        org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);

        // check the resource's media type
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

//        if (RequestMethod.HEAD.equals(request.getMethod())) {
//            setHeaders(response, source, mediaType);
//            return;
//        }

        if (request.getHeader(HttpHeaders.RANGE) != null) {
            writePartialContent(request, response, source, mediaType);
        } else {
            writeContent(response, source, mediaType, resource.getName());
        }
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
