package com.it.songservice.service.unzip;

import com.it.songservice.model.Resource;
import org.springframework.stereotype.Service;

@Service
public interface ResourceUnzipService {

    void upload(Resource resource) throws Exception;
}
