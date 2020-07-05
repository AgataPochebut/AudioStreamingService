package com.epam.songservice.service.storage.Song;

import com.epam.songservice.jms.Producer;
import com.epam.resourceservice.model.Resource;
import com.epam.songservice.model.Song;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class SongConversionDecorator extends SongStorageDecorator {

    private RestTemplate restTemplate;

    private Producer producer;

//    private static List<String> formats = new ArrayList<>();

    public SongConversionDecorator(SongStorageService storageService, RestTemplate restTemplate) {
        super(storageService);
        this.restTemplate = restTemplate;
    }

    public SongConversionDecorator(SongStorageService storageService, Producer producer) {
        super(storageService);
        this.producer = producer;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        List<String> formats = new ArrayList<>();
        formats.add("mp3");

        //отправить id ресурса
        //принять id нового ресурса
        //передать новый ресурс дальше
        //обмениваться json ресурса?
        if (!formats.contains(FilenameUtils.getExtension(resource.getName()))) {
            Resource resource1 = producer.convert(resource, "conv");
            return super.upload(resource1);
        } else return super.upload(resource);
    }

    @Override
    public String test() {
        return super.test() + " Conversion";
    }

}
