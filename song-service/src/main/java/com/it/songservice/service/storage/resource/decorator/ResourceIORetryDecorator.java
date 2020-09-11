package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.exception.DeleteException;
import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;

public class ResourceIORetryDecorator extends ResourceStorageDecorator {

    public ResourceIORetryDecorator(ResourceStorageService storageService) {
        super(storageService);
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        int count = 0;
        while (count<3)
        {
            try {
                return super.upload(source, name);
            }
            catch (Exception e) {

            }
            count++;
        }

        throw new UploadException("IO");
    }

    @Override
    public void delete(Resource resource) throws Exception {
        int count = 0;
        while (count<3)
        {
            try {
                super.delete(resource);
                if(!super.exist(resource)) {
                    return;
                }
            }
            catch (Exception e) {

            }
            count++;
        }

        throw new DeleteException("IO");
    }
}
