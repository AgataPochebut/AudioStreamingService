package com.epam.songservice.service.storage.Song;//package com.epam.service.storage;

public class SongCleanupDecorator extends SongStorageDecorator {

    public SongCleanupDecorator(SongStorageService storageService) {
        super(storageService);
    }
}
