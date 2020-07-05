//package com.epam.songservice.service.storage.Resource;
//
//import com.epam.songservice.model.Resource;
//import com.epam.songservice.service.repository.ResourceRepositoryService;
//import org.apache.commons.codec.digest.DigestUtils;
//
//public class DedupingDecorator extends ResourceStorageDecorator {
//
//    private ResourceRepositoryService repositoryService;
//
//    public DedupingDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
//        super(storageService);
//        this.repositoryService = repositoryService;
//    }
//
//    @Override
//    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
//        if(repositoryService.findByChecksum(DigestUtils.md5Hex(source.getInputStream())) != null) throw new Exception("Exist");
//        else return super.upload(source, name);
//    }
//
//    @Override
//    public String test() {
//        return super.test() + " Deduping";
//    }
//
//}
