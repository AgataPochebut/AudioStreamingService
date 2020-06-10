package com.epam.songservice.service.storage.Song;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.storage.ResourceStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Decorate(SongStorageDecorator.class)
@Service
public class SongStorageServiceImpl implements SongStorageService {

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;
    //or
    @Autowired
    private JmsTemplate jmsTemplate;

    //receive from jms
    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {

        Resource resource = resourceStorageFactory.getService().upload(source, name);

        return Song.builder()
                .resource(resource)
                .build();
    }

    @Override
    public org.springframework.core.io.Resource download(Song entity) throws Exception {
        Resource resource = entity.getResource();
        return resourceStorageFactory.getService().download(resource);
    }

    @Override
    public void delete(Song entity) {
        Resource resource = entity.getResource();
        resourceStorageFactory.getService().delete(resource);

        //or

//        //sync mq
//        Message message = jmsTemplate.sendAndReceive("storage", new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                try {
//                    BytesMessage bytesMessage = session.createBytesMessage();
//                    InputStream fileInputStream = source.getInputStream();
//                    final int BUFLEN = 64;
//                    byte[] buf1 = new byte[BUFLEN];
//                    int bytes_read = 0;
//                    while ((bytes_read = fileInputStream.read(buf1)) != -1) {
//                        bytesMessage.writeBytes(buf1, 0, bytes_read);
//                    }
//                    fileInputStream.close();
//
//                    bytesMessage.setStringProperty("name", name);
//
//                    bytesMessage.setJMSCorrelationID("123");
//
//                    return bytesMessage;
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        });
//
////        ObjectMapper Obj = new ObjectMapper();
////        Resource resource = Obj.readValue(((TextMessage)message).getText(), Resource.class);
////        resource = resourceRepositoryService.save(resource);
//
//        return Song.builder()
////                .resource(resource)
//                .resource_id_1(message.getBody(Long.class))
//                .title("test")
//                .build();
    }

    @Override
    public boolean exist(Song entity) {
        Resource resource = entity.getResource();
        return resourceStorageFactory.getService().exist(resource);
    }

    @Override
    public String test() {
        return "Song";
    }

}
