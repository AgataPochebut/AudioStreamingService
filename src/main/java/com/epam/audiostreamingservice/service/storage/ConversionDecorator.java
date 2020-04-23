package com.epam.audiostreamingservice.service.storage;

import com.epam.audiostreamingservice.feign.conversion.ConversionClient;
import com.epam.audiostreamingservice.model.Resource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ConversionDecorator extends ResourceStorageDecorator {

    private ConversionClient conversionClient;

    public ConversionDecorator(ResourceStorageService storageService, ConversionClient conversionClient) {
        super(storageService);
        this.conversionClient = conversionClient;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {
        if (FilenameUtils.getExtension(source.getFilename()).equals("wav")) {
            source = conversionClient.convert((MultipartFile) source, "mp3").getBody();
        }
        return super.upload(source);
    }

    @Override
    public String test() {
        return super.test() + " Conversion";
    }

}
