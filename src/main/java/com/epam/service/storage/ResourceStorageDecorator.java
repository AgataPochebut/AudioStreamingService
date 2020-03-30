package com.epam.service.storage;

import com.amazonaws.services.dynamodbv2.model.Get;
import com.epam.model.Resource;
import com.epam.model.StorageTypes;
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
    public org.springframework.core.io.Resource download(Long id) {
        return storageService.download(id);
    }

    @Override
    public void delete(Resource resource) {
        storageService.delete(resource);
    }

    @Override
    public void delete(Long id) { storageService.delete(id); }

    @Override
    public boolean exist(Resource resource) {
        return storageService.exist(resource);
    }

    @Override
    public boolean exist(Long id) {
        return storageService.exist(id);
    }

    @Override
    public String make() {
        return storageService.make();
    }

}
