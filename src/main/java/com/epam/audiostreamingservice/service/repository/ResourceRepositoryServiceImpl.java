package com.epam.audiostreamingservice.service.repository;

import com.epam.audiostreamingservice.model.Resource;
import com.epam.audiostreamingservice.jpa_repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceRepositoryServiceImpl extends GenericServiceImpl<Resource, Long> implements ResourceRepositoryService {

    @Autowired
    private ResourceRepository repository;

    @Override
    public Resource findByChecksum(String checksum) {
        return repository.findByChecksum(checksum).orElse(null);
    }

    @Override
    public boolean existByChecksum(String checksum){
        return !repository.findByChecksum(checksum).isEmpty();
    }
}
