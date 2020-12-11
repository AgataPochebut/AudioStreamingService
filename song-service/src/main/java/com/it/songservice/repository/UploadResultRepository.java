package com.it.songservice.repository;

import com.it.songservice.model.UploadResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadResultRepository extends GenericRepository<UploadResult, Long> {

    @Query("SELECT t FROM UploadResult t WHERE t.resource = ?1")
    UploadResult findByResource(Long s);

}
