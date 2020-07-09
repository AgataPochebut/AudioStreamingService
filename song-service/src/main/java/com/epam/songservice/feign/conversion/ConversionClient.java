package com.epam.songservice.feign.conversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "conversion-service", fallbackFactory = ConversionFallbackFactory.class)
public interface ConversionClient {

    @PostMapping(value = "conversion", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<Resource> convert(@PathVariable("data") MultipartFile multipartFile, @RequestParam("format") String format);

}

