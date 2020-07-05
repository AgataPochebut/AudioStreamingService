package com.epam.songservice.jms;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.storage.Song.SongStorageService;
import com.epam.resourceservice.model.Resource;
import com.epam.resourceservice.service.storage.ResourceStorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private SongStorageService songStorageService;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "zip")
    public void upload(ObjectMessage message) throws Exception {

        final List<Song> result = new ArrayList<>();

        Resource resource = (Resource) message.getObject();

        upload(resource, result);

        jmsTemplate.send(message.getJMSReplyTo(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    ObjectMessage sendMessage = session.createObjectMessage();
                    sendMessage.setObject((Serializable) result);

                    return sendMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

    public void upload(Resource resource, List<Song> result) throws Exception {
        //1. zip - уже имеем id ресурса, получаем поток, распаковываем, для каждого файла: создается ресурс, сохраняется в storage, id ресурса закидывается в jms
        //2. файл - уже имеем id ресурса, создаем песню на основании ресурса
        //возвращаем массив песен

        if (FilenameUtils.getExtension(resource.getName()).equals("zip")) {
            org.springframework.core.io.Resource source = resourceStorageFactory.getService().download(resource);

            ZipInputStream zin = new ZipInputStream(source.getInputStream());
            ZipEntry entry;
            String name;
            int c;
            while ((entry = zin.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    name = entry.getName(); // получим название файла

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    IOUtils.copy(zin, out);
                    org.springframework.core.io.Resource source1 = new ByteArrayResource(out.toByteArray());

                    Resource resource1 = null;
                    try {
                        resource1 = resourceStorageFactory.getService().upload(source1, name);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    upload(resource1, result);
                }
                zin.closeEntry();
            }
            resourceStorageFactory.getService().delete(resource);
        } else {
            result.add(songStorageService.upload(resource));
        }
    }
}
