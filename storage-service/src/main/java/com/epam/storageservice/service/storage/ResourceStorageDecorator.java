package com.epam.storageservice.service.storage;

import com.epam.storageservice.model.Resource;

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

//    @Override
//    public org.springframework.core.io.Resource download(Long id) {
//        return storageService.download(id);
//    }

    @Override
    public void delete(Resource resource) {
        storageService.delete(resource);
    }

//    @Override
//    public void delete(Long id) { storageService.delete(id); }

    @Override
    public boolean exist(Resource resource) {
        return storageService.exist(resource);
    }

//    @Override
//    public boolean exist(Long id) {
//        return storageService.exist(id);
//    }

    @Override
    public String test() {
        return storageService.test();
    }

}
