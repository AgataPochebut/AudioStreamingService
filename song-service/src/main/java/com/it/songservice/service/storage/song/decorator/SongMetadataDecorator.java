package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.parse.SongParseService;
import com.it.songservice.service.storage.song.SongStorageService;

public class SongMetadataDecorator extends SongStorageDecorator {

    private SongParseService parseService;

    public SongMetadataDecorator(SongStorageService storageService, SongParseService parseService) {
        super(storageService);
        this.parseService = parseService;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        Song entity = super.upload(resource);
        try {
            Song entity1 = parseService.parse(resource);
            entity.setAlbum(entity1.getAlbum());
            entity.setName(entity1.getName());
//            entity.setNotes(entity1.getNotes());
            return entity;
        }
        catch (Exception e) {
            super.delete(entity);
            throw new UploadException("Metadata exc in "+ resource.getName(), e);
        }
    }

}
