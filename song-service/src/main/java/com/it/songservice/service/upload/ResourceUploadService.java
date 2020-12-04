package com.it.songservice.service.upload;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadResult;
import com.it.songservice.model.UploadStatus;

import javax.jms.JMSException;

public interface ResourceUploadService {

    void upload(Resource resource) throws Exception;

    UploadResult getResultById(Long id) throws JMSException;

    void setStatus(Resource resource, UploadStatus status) throws Exception;

    void setMess(Resource resource, String mess) throws Exception;

    void setCorr(Resource resource, Resource resource1) throws Exception;

}
