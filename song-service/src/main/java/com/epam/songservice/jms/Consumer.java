package com.epam.songservice.jms;

import com.epam.songservice.dto.response.ResourceResponseDto;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.Resource.ResourceStorageFactory;
import com.epam.songservice.service.storage.Song.SongStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private SongRepositoryService songRepositoryService;

    @Autowired
    private SongStorageService songStorageService;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Mapper mapper;

    //принять поток создать ресурс и песню
    @JmsListener(destination = "upload")
    public void upload(BytesMessage message) throws Exception {

        //bytemessage + name => Resource source + name
        //SongStorageService.upload

        byte[] data = new byte[(int) message.getBodyLength()];
        message.readBytes(data);

        org.springframework.core.io.Resource source = new ByteArrayResource(data);
        String name = message.getStringProperty("name");

        if (FilenameUtils.getExtension(name).equals("zip")) {
            try (ZipInputStream zin = new ZipInputStream(source.getInputStream())) {
                ZipEntry entry;
                String filename;
                int c;
                while ((entry = zin.getNextEntry()) != null) {

                    filename = entry.getName(); // получим название файла

                    if (!entry.isDirectory() && FilenameUtils.getExtension(filename).equals("mp3")) {
                        File file = new File("/temp", FilenameUtils.getName(filename));
                        file.getParentFile().mkdirs();
                        try (FileOutputStream fout = new FileOutputStream(file)) {
                            while ((c = zin.read()) >= 0) {
                                fout.write(c);
                            }
                        }

                        songStorageService.upload(new FileSystemResource(file), file.getName());
                    }
                    zin.closeEntry();
                }
            }
        } else {
            songStorageService.upload(source, name);
        }
    }

    @JmsListener(destination = "download")
    public void download(Message message) throws Exception {
        Long id = message.getLongProperty("id");

        Song entity = songRepositoryService.findById(id);
        org.springframework.core.io.Resource source = songStorageService.download(entity);

//        Resource resource = entity.getResource();
//        resource.getName()
    }

    //sync response
    @JmsListener(destination = "storage1")
    public void listen1(BytesMessage message) throws Exception {

        //bytemessage + name => Resource source + name
        //SongStorageService.upload

        byte[] data = new byte[(int) message.getBodyLength()];
        message.readBytes(data);

        org.springframework.core.io.Resource source = new ByteArrayResource(data);
        String name = message.getStringProperty("name");

        Resource entity = resourceStorageFactory.getService().upload(source, name);

        jmsTemplate.send(message.getJMSReplyTo(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    TextMessage textMessage = session.createTextMessage();

                    ObjectMapper Obj = new ObjectMapper();
                    textMessage.setText(Obj.writeValueAsString(mapper.map(entity, ResourceResponseDto.class)));

                    return textMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }


}
