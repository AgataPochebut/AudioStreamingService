package com.it.songservice.service.upload;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadResult;
import com.it.songservice.model.UploadStatus;

public interface UploadResultService {

    void setStatus(Resource resource, UploadStatus status);

    void setStatus(UploadResult result, UploadStatus status);

    void setMess(Resource resource, String mess);

    void setMess(UploadResult result, String mess);

    void receiveStatus(Long upload_id, UploadStatus status) throws Exception;

    void receiveMess(Long upload_id, String mess) throws Exception;

}
