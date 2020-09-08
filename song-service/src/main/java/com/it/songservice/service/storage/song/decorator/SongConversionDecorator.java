package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.ConversionException;
import com.it.songservice.feign.conversion.ConversionClient;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.song.SongStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class SongConversionDecorator extends SongStorageDecorator {

    private ConversionClient conversionClient;

    public SongConversionDecorator(SongStorageService storageService, ConversionClient conversionClient) {
        super(storageService);
        this.conversionClient = conversionClient;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        if (!FilenameUtils.getExtension(name).equals("mp3")) {
//            try {
                MultipartFile multipartFile = new MockMultipartFile(name, name, "multipart/form-data", source.getInputStream());
                ResponseEntity<org.springframework.core.io.Resource> response = conversionClient.convert(multipartFile, "mp3");
                if (response.getStatusCode().isError()) {
                    throw new ConversionException("Error in conversion");
                }
                org.springframework.core.io.Resource source1 = response.getBody();
                String name1 = response.getHeaders().getContentDisposition().getFilename();
                return super.upload(source1, name1);
//            } catch (Exception e) {
//                throw new Exception("Conv exc");
//            }
        } else {
            return super.upload(source, name);
        }
    }

}
