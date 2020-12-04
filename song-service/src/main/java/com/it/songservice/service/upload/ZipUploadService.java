package com.it.songservice.service.upload;

import com.it.songservice.model.Resource;

public interface ZipUploadService {

    void upload(Resource resource) throws Exception;
}
