package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.service.repository.ResourceRepositoryService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public class DedupingDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public DedupingDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(MultipartFile file) throws Exception {
        Resource resource = repositoryService.findByChecksum(file.hashCode());
        if(resource!=null) return resource;
        else return super.upload(file);
    }

    @Override
    public String make() {
        return super.make() + "Deduping";
    }

}
