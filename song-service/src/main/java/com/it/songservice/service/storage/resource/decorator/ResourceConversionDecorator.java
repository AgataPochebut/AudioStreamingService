package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.service.conversion.ResourceConversionService;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import org.apache.commons.io.FilenameUtils;

public class ResourceConversionDecorator extends ResourceStorageDecorator {

    private ResourceConversionService conversionService;

    public ResourceConversionDecorator(ResourceStorageService storageService, ResourceConversionService conversionService) {
        super(storageService);
        this.conversionService = conversionService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource entity = super.upload(source, name);
        if (FilenameUtils.getExtension(name).equalsIgnoreCase("wav")) {
            try {
                return conversionService.convert(entity, "mp3");
            } catch (Exception e) {
                super.delete(entity);
                throw new UploadException("Conv exc in " + name, e);
            }
        }
        return entity;
    }

}
