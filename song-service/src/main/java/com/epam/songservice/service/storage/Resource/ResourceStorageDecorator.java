//package com.epam.songservice.service.storage.Resource;
//
//import com.epam.songservice.model.Resource;
//
//public abstract class ResourceStorageDecorator implements ResourceStorageService {
//
//    protected ResourceStorageService storageService;
//
//    public ResourceStorageDecorator(ResourceStorageService storageService){
//        this.storageService = storageService;
//    }
//
//    @Override
//    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
//        return storageService.upload(source, name);
//    }
//
//    @Override
//    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
//        return storageService.download(resource);
//    }
//
//    @Override
//    public void delete(Resource resource) {
//        storageService.delete(resource);
//    }
//
//    @Override
//    public boolean exist(Resource resource) {
//        return storageService.exist(resource);
//    }
//
//    @Override
//    public String test() {
//        return storageService.test();
//    }
//
//}
