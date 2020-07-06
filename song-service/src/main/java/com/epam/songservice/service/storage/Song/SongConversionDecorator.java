package com.epam.songservice.service.storage.Song;

import com.epam.songservice.feign.conversion.ConversionClient;
import com.epam.songservice.jms.Producer;
import com.epam.songservice.model.Song;
import com.epam.storageservice.model.Resource;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

public class SongConversionDecorator extends SongStorageDecorator {

    private ConversionClient conversionClient;

    private Producer producer;

//    private static List<String> formats = new ArrayList<>();

    public SongConversionDecorator(SongStorageService storageService, ConversionClient conversionClient) {
        super(storageService);
        this.conversionClient = conversionClient;
    }

    public SongConversionDecorator(SongStorageService storageService, Producer producer) {
        super(storageService);
        this.producer = producer;
    }

    @Override
    public Song upload1(Resource resource) throws Exception {
        List<String> formats = new ArrayList<>();
        formats.add("mp3");

        if (!formats.contains(FilenameUtils.getExtension(resource.getName()))) {
            Resource resource1 = producer.convert(resource, "conv");
            return super.upload1(resource1);
        } else return super.upload1(resource);
    }

    @Override
    public String test() {
        return super.test() + " Conversion";
    }

}
