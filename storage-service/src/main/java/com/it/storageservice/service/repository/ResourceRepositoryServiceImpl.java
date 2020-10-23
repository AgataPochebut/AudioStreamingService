package com.it.storageservice.service.repository;

import com.it.songservice.model.Resource;
import com.it.songservice.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceRepositoryServiceImpl extends GenericRepositoryServiceImpl<Resource, Long> implements ResourceRepositoryService {

    @Autowired
    private ResourceRepository repository;

    @Override
    public Resource findByChecksum(String s) {
        return repository.findByChecksum(s).orElse(null);
    }

}
