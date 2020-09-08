package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.model.Resource;
import com.it.songservice.service.repository.ResourceRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;

//@Service
//@Scope(value = "prototype")
public class ResourceDBDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public ResourceDBDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    public ResourceDBDecorator(ResourceStorageService storageService, ApplicationContext context) {
        super(storageService);
        this.repositoryService = context.getBean(ResourceRepositoryService.class);
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        if(repositoryService.findByChecksum(DigestUtils.md5Hex(source.getInputStream()))!=null) {
            throw new Exception("Already exist file in db");
        }

        Resource entity = super.upload(source, name);

        try {
           entity = repositoryService.save(entity);
           return entity;
        }
        catch (Exception e){
            super.delete(entity);
            throw new Exception("Error when save file to db");
        }
    }

    @Override
    public void delete(Resource entity) throws Exception {
        try {
            repositoryService.deleteById(entity.getId());
        }
        catch (Exception e){
            throw new Exception("Error when delete file from db");
        }

        super.delete(entity);
    }

    @Override
    public boolean exist(Resource entity) {
        if(repositoryService.existById(entity.getId())) return super.exist(entity);
        else return false;
    }
}
