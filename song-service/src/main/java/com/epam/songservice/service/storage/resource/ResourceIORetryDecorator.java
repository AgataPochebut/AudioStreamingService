package com.epam.songservice.service.storage.resource;

import com.epam.songservice.model.Resource;

public class ResourceIORetryDecorator extends ResourceStorageDecorator {

    public ResourceIORetryDecorator(ResourceStorageService storageService) {
        super(storageService);
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = null;
        int count = 0;
        while (resource==null)
        {
            if(count>=3) throw new Exception("Can't upload file");

            try {
                resource = super.upload(source, name);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            count++;
        }
        return resource;
    }

    @Override
    public void delete(Resource resource) throws Exception {
        int count = 0;
        while (super.exist(resource))
        {
            if(count>=3) throw new Exception("Can't delete file");

            try {
                super.delete(resource);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            count++;
        }
    }

    @Override
    public String test() {
        return super.test() + "IORetry";
    }

}
