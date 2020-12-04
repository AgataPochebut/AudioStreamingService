package com.it.songservice.service.storage.song;

import com.it.songservice.annotation.Decorate;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.song.decorator.SongDBDecorator;
import com.it.songservice.service.storage.song.decorator.SongIndexDecorator;
import com.it.songservice.service.storage.song.decorator.SongMetadataDecorator;

@Decorate(decorations = {SongMetadataDecorator.class, SongDBDecorator.class, SongIndexDecorator.class})
public interface SongStorageService {

    Song upload(Resource resource) throws Exception;

    void delete(Song entity) throws Exception;

}
