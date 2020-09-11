package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.service.repository.ResourceRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import org.apache.commons.codec.digest.DigestUtils;

public class ResourceDedupingDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public ResourceDedupingDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        if (repositoryService.findByChecksum(DigestUtils.md5Hex(source.getInputStream())) == null) {
            return super.upload(source, name);
        }

        throw new UploadException("Deduping");
    }
}
