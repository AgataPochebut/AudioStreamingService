package com.epam.songservice.feign.conversion;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@AllArgsConstructor
public class ConversionFallback implements ConversionClient {

    private Throwable throwable;

    @Override
    public ResponseEntity<Resource> convert(MultipartFile multipartFile, String format) {
        String errorMessage = throwable.getMessage();
        log.error(errorMessage, throwable);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
