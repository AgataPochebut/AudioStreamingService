package com.epam.service;

import com.epam.model.Song;
import com.epam.service.repository.SongRepositoryService;
import com.epam.service.storage.ResourceStorageFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class SongServiceImpl implements SongService{

    @Autowired
    private SongRepositoryService repositoryService;

    @Autowired
    private ResourceStorageFactory storageServiceFactory;

    public Song upload(Resource source) throws Exception {

        com.epam.model.Resource resource = storageServiceFactory.getService().upload(source);
        Song entity = repositoryService.save(Song.builder()
                .resource(resource)
                .build());

        return entity;
    }

    @Async
    public CompletableFuture<Song> uploadAsync(Resource source) throws Exception {
        return CompletableFuture.completedFuture(upload(source));
    }

    public List<Song> uploadZip(Resource source) throws Exception {

        final List<Song> entity = new ArrayList<>();

        try (ZipInputStream zin = new ZipInputStream(source.getInputStream())) {
            ZipEntry entry;
            String name;
            int c;
            while ((entry = zin.getNextEntry()) != null) {

                name = entry.getName(); // получим название файла

                if (!entry.isDirectory() && FilenameUtils.getExtension(name).equals("mp3")) {
                    File file = new File("/temp", FilenameUtils.getName(name));
                    file.getParentFile().mkdirs();
                    try (FileOutputStream fout = new FileOutputStream(file)) {
                        while ((c = zin.read()) >= 0) {
                            fout.write(c);
                        }
                    }

                    com.epam.model.Resource resource = storageServiceFactory.getService().upload(new FileSystemResource(file));
                    entity.add(repositoryService.save(Song.builder()
                            .resource(resource)
                            .build()));
                }
                zin.closeEntry();
            }
        }
        return entity;
    }

    @Async
    public CompletableFuture<List<Song>> uploadZipAsync(Resource source) throws Exception {
        return CompletableFuture.completedFuture(uploadZip(source));
    }

    public void delete(Long id){
        Song entity = repositoryService.findById(id);
        com.epam.model.Resource resource = entity.getResource();

        repositoryService.deleteById(id);
        storageServiceFactory.getService().delete(entity.getResource());
    }
}
