package com.epam.songservice.service.storage;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SongStorageServiceImpl implements SongStorageService {


    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

//    @Autowired
//    private ResourceStorageService storageService;

    @Override
    public org.springframework.core.io.Resource download(Song entity) throws Exception {
        Resource resource = entity.getResource();
        return resourceStorageFactory.getService().download(resource);
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = resourceStorageFactory.getService().upload(source, name);
        return Song.builder()
                .resource(resource)
                .title("test")
                .build();
    }

    @Override
    public void delete(Song entity) {
        Resource resource = entity.getResource();
        resourceStorageFactory.getService().delete(resource);
    }

    @Override
    public boolean exist(Song entity) {
        Resource resource = entity.getResource();
        return resourceStorageFactory.getService().exist(resource);
    }

    @Override
    public String test() {
        return "Song";
    }

}
