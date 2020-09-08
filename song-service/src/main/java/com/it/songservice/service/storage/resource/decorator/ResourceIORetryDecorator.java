package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import org.springframework.context.ApplicationContext;

public class ResourceIORetryDecorator extends ResourceStorageDecorator {

    public ResourceIORetryDecorator(ResourceStorageService storageService) {
        super(storageService);
    }

    public ResourceIORetryDecorator(ResourceStorageService storageService, ApplicationContext context) {
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
}
