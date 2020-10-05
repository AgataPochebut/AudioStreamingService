package com.it.songservice.service.storage.song;

import com.it.songservice.jms.Producer;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongStorageServiceImpl implements SongStorageService {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Producer producer;

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = resourceStorageServiceManager.upload(source, name);

        return Song.builder()
            .resource(resource)
            .build();
    }

    @Override
    public List<Song> uploadZip(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = resourceStorageServiceManager.upload(source, name);

        try {
            return producer.upload(resource);
        } finally {
            resourceStorageServiceManager.delete(resource);
        }
    }

    @Override
    public org.springframework.core.io.Resource download(Song entity) throws Exception {
        Resource resource = entity.getResource();
        return resourceStorageServiceManager.download(resource);
    }

    @Override
    public void delete(Song entity) throws Exception {
        Resource resource = entity.getResource();
        resourceStorageServiceManager.delete(resource);
    }

    @Override
    public boolean exist(Song entity) throws Exception {
        Resource resource = entity.getResource();
        return resourceStorageServiceManager.exist(resource);
    }

}