package com.epam.storageservice.service.storage;

import com.epam.storageservice.model.Resource;

public class IORetryDecorator extends ResourceStorageDecorator {

    public IORetryDecorator(ResourceStorageService storageService) {
        super(storageService);
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = null;
        while (resource==null || !super.exist(resource))
        {
            resource = super.upload(source, name);
        }
        return resource;
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
        if (!super.exist(resource)) throw new Exception("Not exist");
        return super.download(resource);
    }

    @Override
    public void delete(Resource resource) {
        while (super.exist(resource))
        {
            super.delete(resource);
        }
    }

    @Override
    public String test() {
        return super.test() + "IORetry";
    }

}
