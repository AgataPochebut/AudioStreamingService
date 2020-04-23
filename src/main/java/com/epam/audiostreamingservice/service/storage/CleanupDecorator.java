//package com.epam.streamingservice.service.storage;
//
//import com.epam.streamingservice.model.Resource;
//import com.epam.streamingservice.service.repository.ResourceRepositoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//public class CleanupDecorator extends ResourceStorageDecorator {
//
//    private ResourceRepositoryService repositoryService;
//
//    public CleanupDecorator(ResourceStorageService service){
//        super(service);
//    }
//
//    @Override
//    public Resource upload(MultipartFile file) throws Exception {
//        return super.upload(file);
//    }
//}
