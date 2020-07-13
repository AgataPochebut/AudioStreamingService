package com.epam.songservice.service.storage.Resource;

import com.epam.songservice.model.Resource;
import com.epam.songservice.service.repository.ResourceRepositoryService;
import org.apache.commons.codec.digest.DigestUtils;

public class ResourceDBDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public ResourceDBDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
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
    public String test() {
        return super.test() + " DBInsert";
    }

}
