package com.epam.songservice.jms;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.ResourceRepositoryService;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.songservice.service.storage.Resource.ResourceStorageFactory;
import com.epam.songservice.service.storage.Song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
    private ResourceRepositoryService resourceRepositoryService;

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
    @JmsListener(destination = "zip")
    public void zip(Message message) throws Exception {
        Long id = message.getLongProperty("id");

        com.epam.songservice.model.Resource resource = resourceRepositoryService.findById(id);
        Resource source = resourceStorageFactory.getService().download(resource);

        final List<Song> entity = new ArrayList<>();

        try (ZipInputStream zin = new ZipInputStream(source.getInputStream())) {
            ZipEntry entry;
            String name;
            int c;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName(); // получим название файла

                if (!entry.isDirectory() && FilenameUtils.getExtension(name).equals("mp3")) {
//                    File file = new File("/temp", FilenameUtils.getName(name));
//                    file.getParentFile().mkdirs();
//                    try (FileOutputStream fout = new FileOutputStream(file)) {
//                        while ((c = zin.read()) >= 0) {
//                            fout.write(c);
//                        }
//                    }

                    //загрузит оперативу. попробовать побайтово
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    IOUtils.copy(zin, out);

                    entity.add(songStorageService.upload(new ByteArrayResource(out.toByteArray()), name));
                }
                zin.closeEntry();
            }
        }

        finally{
            message.setJMSDestination(null);
            resourceStorageFactory.getService().delete(resource);
        }

        jmsTemplate.send(message.getJMSReplyTo(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    ObjectMessage sendMessage = session.createObjectMessage();
                    sendMessage.setObject((Serializable) entity);

                    return sendMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }


}
