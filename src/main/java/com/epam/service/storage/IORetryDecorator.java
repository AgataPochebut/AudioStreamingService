package com.epam.service.storage;

import com.epam.model.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

public class IORetryDecorator extends ResourceStorageDecorator {

    public IORetryDecorator(ResourceStorageService storageService) {
        super(storageService);
    }

    @Override
    public Resource upload(MultipartFile file) throws Exception {
        Resource resource;
        do{
            resource = super.upload(file);
        }
        while (resource==null || !super.exist(resource));
        return resource;
    }

    @Override
    public void delete(Resource resource) {
        do{
            super.delete(resource);
        }
        while (super.exist(resource));
    }

    @Override
    public void delete(Long id) {
        do{
            super.delete(id);
        }
        while (super.exist(id));
    }

    @Override
    public String make() {
        return super.make() + "IORetry";
    }

}
