package com.it.songservice.service.repository;

import com.it.songservice.model.UploadResult;

public interface UploadResultRepositoryService extends GenericRepositoryService<UploadResult, Long> {

    UploadResult findByResource(Long s);

}
