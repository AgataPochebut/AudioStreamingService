package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.feign.conversion.ConversionClient;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import com.it.songservice.service.storage.song.SongStorageService;
import com.it.songservice.service.storage.song.decorator.SongStorageDecorator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ResourceConversionDecorator extends ResourceStorageDecorator {

    private ConversionClient conversionClient;

    public ResourceConversionDecorator(ResourceStorageService storageService, ConversionClient conversionClient) {
        super(storageService);
        this.conversionClient = conversionClient;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Throwable lastException;
        if (FilenameUtils.getExtension(name).equalsIgnoreCase("wav")) {
            try {
                MultipartFile multipartFile = new MockMultipartFile(name, name, "multipart/form-data", source.getInputStream());
                ResponseEntity<org.springframework.core.io.Resource> response = conversionClient.convert(multipartFile, "mp3");
                source = response.getBody();
                name = response.getHeaders().getContentDisposition().getFilename();
                return super.upload(source, name);
            } catch (Exception e) {
                lastException = e;
            }
        } else return super.upload(source, name);
        throw new UploadException("Conv exc in " + name, lastException);
    }

}
