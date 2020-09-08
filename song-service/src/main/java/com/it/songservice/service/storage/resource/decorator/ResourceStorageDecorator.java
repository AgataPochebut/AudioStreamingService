package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;

public abstract class ResourceStorageDecorator implements ResourceStorageService {

    protected ResourceStorageService storageService;

    public ResourceStorageDecorator(ResourceStorageService storageService){
        this.storageService = storageService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        return storageService.upload(source, name);
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
        return storageService.download(resource);
    }

    @Override
    public void delete(Resource resource) throws Exception {
        storageService.delete(resource);
    }

    @Override
    public boolean exist(Resource resource) {
        return storageService.exist(resource);
    }

    @Override
    public boolean supports(Class clazz) {
        return storageService.supports(clazz);
    }

}
