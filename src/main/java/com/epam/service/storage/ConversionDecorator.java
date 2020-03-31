package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.service.conversion.ConversionService;
import org.springframework.web.multipart.MultipartFile;

public class ConversionDecorator extends ResourceStorageDecorator {

    private ConversionService conversionService;

    public ConversionDecorator(ResourceStorageService storageService, ConversionService conversionService) {
        super(storageService);
        this.conversionService = conversionService;
    }

    @Override
    public Resource upload(MultipartFile file) throws Exception {
        Resource resource;
        do{
            resource = super.upload(file);
        }
        while (resource==null || !super.exist(resource));
        return resource;
    }

    @Override
    public String make() {
        return super.make() + "Conversion";
    }

}
