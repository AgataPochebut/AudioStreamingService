package com.epam.service.storage;

import com.epam.model.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

public class IORetryDecorator extends ResourceStorageDecorator {

    public IORetryDecorator(ResourceStorageService storageService) {
        super(storageService);
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {
        Resource resource = null;
        while (resource==null || !super.exist(resource))
        {
            resource = super.upload(source);
        }
        return resource;
    }

    @Override
    public void delete(Resource resource) {
        while (super.exist(resource))
        {
            super.delete(resource);
        }
    }

//    @Override
//    public void delete(Long id) {
//        do{
//            super.delete(id);
//        }
//        while (super.exist(id));
//    }

    @Override
    public String test() {
        return super.test() + "IORetry";
    }

}
