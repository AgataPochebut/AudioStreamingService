//package com.epam.songservice.service.storage.Resource;
//
//import com.epam.songservice.model.Resource;
//import org.springframework.cache.CacheManager;
//
//public class ResourceCleanupDecorator extends ResourceStorageDecorator {
//
//    private CacheManager cacheManager;
//
//    public ResourceCleanupDecorator(ResourceStorageService storageService, CacheManager cacheManager) {
//        super(storageService);
//        this.cacheManager = cacheManager;
//    }
//
//    @Override
//    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
//        try {
//            return super.upload(source, name);
//        } catch (Exception e) {
//            su
//            throw new Exception("Upload exc");}
//    }
//
//    @Override
//    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
////        return super.download(resource);
//        org.springframework.core.io.Resource source = null;
//        source = cacheManager.getCache("resources").get(resource, org.springframework.core.io.Resource.class);
//        if (source==null){
//            source = super.download(resource);
//            cacheManager.getCache("resources").put(resource, source);
//        }
//        return source;
//    }
//
//    @Override
//    public void delete(Resource resource) throws Exception {
//        cacheManager.getCache("resources").evictIfPresent(resource);
//        super.delete(resource);
//    }
//
//    @Override
//    public String test() {
//        return super.test() + " Cache";
//    }
//
//}
