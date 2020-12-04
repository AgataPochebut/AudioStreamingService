package com.it.songservice.service.upload;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadResult;
import com.it.songservice.model.UploadStatus;

import javax.jms.JMSException;

public interface ResourceUploadService {

    void upload(Resource resource) throws Exception;

    UploadResult getResultById(Long upload_id) throws JMSException;

    void setStatus(Long upload_id, UploadStatus status) throws Exception;

    void setMess(Long upload_id, String mess) throws Exception;

    void setCorr(Long upload_id, Long corr_upload_id) throws Exception;

}
