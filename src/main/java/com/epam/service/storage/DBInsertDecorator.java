package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.service.repository.ResourceRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

public class DBInsertDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public DBInsertDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {
        Resource resource = super.upload(source);
        return repositoryService.save(resource);
    }

    @Override
    public void delete(Resource resource) {
        super.delete(resource);
        repositoryService.deleteById(resource.getId());
    }

//    @Override
//    public void delete(Long id) {
//        super.delete(id);
//        repositoryService.deleteById(id);
//    }

    @Override
    public String test() {
        return super.test() + "DBInsert";
    }

}
