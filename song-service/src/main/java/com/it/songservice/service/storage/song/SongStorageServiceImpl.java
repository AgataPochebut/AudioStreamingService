package com.it.songservice.service.storage.song;

import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.upload.ResourceUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongStorageServiceImpl implements SongStorageService {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private ResourceUploadService resourceUploadService;

    @Override
    public Song upload(Resource resource) throws Exception {
        return Song.builder()
                .resource(resource)
                .build();
    }

    @Override
    public void delete(Song entity) throws Exception {
        Resource resource = entity.getResource();
        resourceStorageServiceManager.delete(resource);
    }

}
