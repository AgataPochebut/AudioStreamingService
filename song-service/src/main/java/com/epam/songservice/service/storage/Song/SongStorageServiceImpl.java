package com.epam.songservice.service.storage.Song;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.model.Song;
import com.epam.resourceservice.model.Resource;
import com.epam.resourceservice.service.storage.ResourceStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Decorate(SongStorageDecorator.class)
@Service
public class SongStorageServiceImpl implements SongStorageService {

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    //receive from jms
    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = resourceStorageFactory.getService().upload(source, name);
        return Song.builder()
                .resource(resource)
                .build();
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        return Song.builder()
                .resource(resource)
                .build();
    }

    @Override
    public org.springframework.core.io.Resource download(Song entity) throws Exception {
        Resource resource = entity.getResource();
        return resourceStorageFactory.getService().download(resource);
    }

//    @Override
//    public Resource download(Song entity) throws Exception {
//        Resource resource = entity.getResource();
//        return resourceStorageFactory.getService().download(resource);
//    }

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
