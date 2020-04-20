package com.epam.service.storage;

import com.epam.model.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class CacheDecorator extends ResourceStorageDecorator {

//    private ResourceRepositoryService repositoryService;

    public CacheDecorator(ResourceStorageService storageService) {
        super(storageService);
//        this.repositoryService = repositoryService;
    }

    @Override
    @CachePut
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {
        return super.upload(source);
    }

    @Override
    @Cacheable
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
        return super.download(resource);
    }

    @Override
    @CacheEvict
    public void delete(Resource resource) {
        super.delete(resource);
    }

    @Override
    public String test() {
        return super.test() + " Cache";
    }

}
