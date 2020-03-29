package com.epam.service.repository;

import com.epam.model.Resource;
import com.epam.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceRepositoryServiceImpl extends GenericServiceImpl<Resource, Long> implements ResourceRepositoryService {

    @Autowired
    private ResourceRepository repository;

    @Override
    public Resource findByChecksum(int checksum) {
        return repository.findByChecksum(checksum);
    }
}
