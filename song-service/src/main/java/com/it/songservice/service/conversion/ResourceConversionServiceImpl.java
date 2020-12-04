package com.it.songservice.service.conversion;

import com.it.songservice.feign.conversion.ConversionClient;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ResourceConversionServiceImpl implements ResourceConversionService {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private ConversionClient conversionClient;

    @Override
    public Resource convert(Resource entity, String format) throws Exception {
        String name = entity.getName();
        org.springframework.core.io.Resource source = resourceStorageServiceManager.download(entity);

        MultipartFile multipartFile = new MockMultipartFile(name, name, "multipart/form-data", source.getInputStream());
        ResponseEntity<org.springframework.core.io.Resource> response = conversionClient.convert(multipartFile, format);
        source = response.getBody();
        name = response.getHeaders().getContentDisposition().getFilename();
        return resourceStorageServiceManager.upload(source, name);

    }
}
