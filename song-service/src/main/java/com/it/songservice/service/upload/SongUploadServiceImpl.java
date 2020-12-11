package com.it.songservice.service.upload;

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
    private SongStorageService songStorageService;

    @Autowired
    private UploadResultService uploadResultService;

    @Override
    public void upload(Resource resource) throws Exception {
        try {
            uploadResultService.setStatus(resource, UploadStatus.PROCEEDED);
            songStorageService.upload(resource);
            uploadResultService.setStatus(resource, UploadStatus.FINISHED);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);

            uploadResultService.setStatus(resource, UploadStatus.FAILED);
            uploadResultService.setMess(resource, errorMessage);
        }
    }

}
