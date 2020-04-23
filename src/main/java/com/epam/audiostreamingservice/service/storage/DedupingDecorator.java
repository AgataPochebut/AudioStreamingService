package com.epam.audiostreamingservice.service.storage;

import com.epam.audiostreamingservice.model.Resource;
import com.epam.audiostreamingservice.service.repository.ResourceRepositoryService;
import org.apache.commons.codec.digest.DigestUtils;

public class DedupingDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public DedupingDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {
        if(repositoryService.existByChecksum(DigestUtils.md5Hex(source.getInputStream()))) throw new Exception("Exist");
        else return super.upload(source);
    }

    @Override
    public String test() {
        return super.test() + " Deduping";
    }

}
