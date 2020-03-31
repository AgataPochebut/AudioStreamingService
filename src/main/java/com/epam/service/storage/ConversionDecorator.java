package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.service.conversion.ConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;

public class ConversionDecorator extends ResourceStorageDecorator {

    private ConversionService conversionService;

    public ConversionDecorator(ResourceStorageService storageService, ConversionService conversionService) {
        super(storageService);
        this.conversionService = conversionService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {

        org.springframework.core.io.Resource newsource = conversionService.convert(source, "mp3");
        return super.upload(newsource);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));//ожидаем ответа
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);//передаем
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("data", source);
//        body.add("format", "mp3");
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        String requestUrl = "http://localhost:8080/conversion";
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<org.springframework.core.io.Resource> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, org.springframework.core.io.Resource.class);
//
//        org.springframework.core.io.Resource resource = responseEntity.getBody();
        //записать на диск
        //return super.upload(new FileSystemResource(resource.getPath()););
    }

    @Override
    public String test() {
        return super.test() + "Conversion";
    }

}
