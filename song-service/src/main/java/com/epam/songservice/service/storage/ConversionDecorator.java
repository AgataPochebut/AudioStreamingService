package com.epam.songservice.service.storage;

import com.epam.songservice.feign.conversion.ConversionClient;
import com.epam.songservice.model.Resource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ConversionDecorator extends ResourceStorageDecorator {

    private ConversionClient conversionService;

    public ConversionDecorator(ResourceStorageService storageService, ConversionClient conversionService) {
        super(storageService);
        this.conversionService = conversionService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {
        if (FilenameUtils.getExtension(source.getFilename()).equals("wav")) {
            source = conversionService.convert((MultipartFile) source, "mp3").getBody();
        }
        return super.upload(source);
    }

    @Override
    public String test() {
        return super.test() + " Conversion";
    }

}
