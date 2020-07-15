package com.epam.songservice.service.storage.Resource;

import com.epam.songservice.model.Resource;
import org.springframework.cache.CacheManager;

public class ResourceCacheDecorator extends ResourceStorageDecorator {

    private CacheManager cacheManager;

    public ResourceCacheDecorator(ResourceStorageService storageService, CacheManager cacheManager) {
        super(storageService);
        this.cacheManager = cacheManager;
    }

    @Override
//    @CachePut(cacheNames = "cachetest")
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = super.upload(source, name);

        cacheManager.getCache("resources").put(resource.getId(), source);
        return resource;
//        return super.upload(source);
    }

    @Override
//    @Cacheable(cacheNames = "cachetest")
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
        org.springframework.core.io.Resource source = null;
        source = cacheManager.getCache("resources").get(resource.getId(), org.springframework.core.io.Resource.class);
        if (source==null){
            source = super.download(resource);
            cacheManager.getCache("resources").put(resource.getId(), source);
        }
        return source;
//        return super.download(resource);
    }

    @Override
//    @CacheEvict(cacheNames = "cachetest")
    public void delete(Resource resource) throws Exception {
        cacheManager.getCache("resources").evictIfPresent(resource.getId());

        super.delete(resource);
    }

    @Override
    public String test() {
        return super.test() + " Cache";
    }

}
