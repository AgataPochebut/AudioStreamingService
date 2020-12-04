package com.it.songservice.service.upload;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.storage.song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SongUploadServiceImpl implements SongUploadService {

    @Autowired
    private ResourceUploadService resourceUploadService;

    @Autowired
    private SongStorageService songStorageService;

    @Override
    public void upload(Resource resource) throws Exception {

        try {
            songStorageService.upload(resource);

            resourceUploadService.setStatus(resource.getId(), UploadStatus.FINISHED);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);

            resourceUploadService.setMess(resource.getId(), errorMessage);
            resourceUploadService.setStatus(resource.getId(),UploadStatus.FAILED);

            throw new UploadException("Can't upload song " + resource.getName(), e);
        }
    }

}
