package com.epam.service.storage;

import com.amazonaws.services.dynamodbv2.model.Get;
import com.epam.model.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

public abstract class ResourceStorageDecorator implements ResourceStorageService {

    protected ResourceStorageService storageService;

    public ResourceStorageDecorator(ResourceStorageService storageService){
        this.storageService = storageService;
    }

    @Override
    public Resource upload(MultipartFile file) throws Exception {
        return storageService.upload(file);
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) {
        return storageService.download(resource);
    }

    @Override
    public void delete(Resource resource) {
        storageService.delete(resource);
    }

    @Override
    public boolean exist(Resource resource) {
        return storageService.exist(resource);
    }

    @Override
    public String make() {
        return storageService.make();
    }

    public Class GetStorageService() {
        return storageService.getClass();
    }
}
