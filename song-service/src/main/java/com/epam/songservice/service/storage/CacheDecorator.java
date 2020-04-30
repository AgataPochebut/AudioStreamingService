package com.epam.songservice.service.storage;

import com.epam.songservice.model.Resource;
import org.springframework.cache.CacheManager;

public class CacheDecorator extends ResourceStorageDecorator {
//можно сюда вставит репозиторий кэша

    private CacheManager cacheManager;

    public CacheDecorator(ResourceStorageService storageService, CacheManager cacheManager) {
        super(storageService);
        this.cacheManager = cacheManager;
    }

    @Override
//    @CachePut(cacheNames = "cachetest")
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
//        return super.upload(source);
        Resource resource = super.upload(source, name);
        cacheManager.getCache("test").put(source, resource);
        return resource;
    }

    @Override
//    @Cacheable(cacheNames = "cachetest")
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
//        return super.download(resource);
        org.springframework.core.io.Resource source = null;
        source = cacheManager.getCache("test").get(resource, org.springframework.core.io.Resource.class);
        if (source==null){
            source = super.download(resource);
            cacheManager.getCache("test").put(resource, source);
        }
        return source;
    }

    @Override
//    @CacheEvict(cacheNames = "cachetest")
    public void delete(Resource resource) {
        cacheManager.getCache("test").evictIfPresent(resource);
        super.delete(resource);
    }

    @Override
    public String test() {
        return super.test() + " Cache";
    }

}
