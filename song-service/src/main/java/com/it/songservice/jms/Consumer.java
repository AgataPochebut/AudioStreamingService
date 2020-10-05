package com.it.songservice.jms;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private SongStorageService songStorageService;

    @JmsListener(destination = "upl")
    @Async
    public void upload(ObjectMessage message) throws Exception {
        Resource resource = (Resource) message.getObject();
        Object result = uploadZip(resource);

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

    public Object uploadZip(Resource resource) throws Exception {
        final List<Song> list = new ArrayList<>();

        Throwable lastException;
        try {
            org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
            ZipInputStream zin = new ZipInputStream(source.getInputStream());
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    byte[] content = IOUtils.toByteArray(zin);
                    org.springframework.core.io.Resource source1 = new ByteArrayResource(content);
                    String name1 = FilenameUtils.getName(entry.getName());
                    if (FilenameUtils.getExtension(name1).equals("zip")) {
                        list.addAll(songStorageService.uploadZip(source1, name1));
                    } else {
                        list.add(songStorageService.upload(source1, name1));
                    }
                }
                zin.closeEntry();
            }
            zin.close();
            return list;
        } catch (Exception e) {
            for (Song i : list) {
                songStorageService.delete(i);
            }
            lastException = e;
        }
        return new UploadException("Zip exc in " + resource.getName(), lastException);
    }

}
