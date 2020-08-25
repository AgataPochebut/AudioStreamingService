package com.epam.songservice.service.storage.song;

import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.exception.ConversionException;
import com.epam.songservice.feign.conversion.ConversionClient;
import com.epam.songservice.jms.Producer;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.storage.resource.ResourceStorageFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class SongConversionDecorator extends SongStorageDecorator {

    private ConversionClient conversionClient;

    private ResourceStorageFactory resourceStorageFactory;

    private Producer producer;

    public SongConversionDecorator(SongStorageService storageService, ConversionClient conversionClient) {
        super(storageService);
        this.conversionClient = conversionClient;
    }

    public SongConversionDecorator(SongStorageService storageService, ConversionClient conversionClient, ResourceStorageFactory resourceStorageFactory) {
        super(storageService);
        this.conversionClient = conversionClient;
        this.resourceStorageFactory = resourceStorageFactory;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        if (!FilenameUtils.getExtension(resource.getName()).equals("mp3")) {
            Resource resource1 = null;

            try {
                org.springframework.core.io.Resource source = resourceStorageFactory.getService(resource.getClass().getAnnotation(StorageType.class).value()).download(resource);
                String name = resource.getName();

                //convert
                MultipartFile multipartFile = new MockMultipartFile(name, name, "multipart/form-data", source.getInputStream());
                ResponseEntity<org.springframework.core.io.Resource> response = conversionClient.convert(multipartFile, "mp3");
                if (response.getStatusCode().isError()) {
                    throw new ConversionException("Error in conversion");
                }
                org.springframework.core.io.Resource source1 = response.getBody();
                String name1 = FilenameUtils.removeExtension(name) + "." + "mp3";
                //save new
                resource1 = resourceStorageFactory.getService().upload(source1, name1);
                //rm old
                resourceStorageFactory.getService(resource.getClass().getAnnotation(StorageType.class).value()).delete(resource);
            } catch (Exception e) {
                resourceStorageFactory.getService(resource.getClass().getAnnotation(StorageType.class).value()).delete(resource);
                throw new Exception("Conv exc");
            }

            return super.upload(resource1);
        } else {
            return super.upload(resource);
        }
    }

    @Override
    public String test() {
        return super.test() + " Conversion";
    }

}
