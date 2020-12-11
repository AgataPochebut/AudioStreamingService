package com.it.songservice.service.repository;

import com.it.songservice.model.UploadResult;
import com.it.songservice.repository.UploadResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UploadResultRepositoryServiceImpl extends GenericRepositoryServiceImpl<UploadResult, Long> implements UploadResultRepositoryService {

    @Autowired
    private UploadResultRepository repository;

    @Override
    public UploadResult findByResource(Long s) {
        return repository.findByResource(s);
    }

}
