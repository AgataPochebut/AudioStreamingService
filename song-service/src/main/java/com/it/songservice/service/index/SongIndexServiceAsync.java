package com.it.songservice.service.index;

import com.it.songservice.model.Song;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import javax.xml.bind.DatatypeConverter;

@Service
@Slf4j
public class SongIndexServiceAsync implements SongIndexService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void save(Song entity) {
        jmsTemplate.convertAndSend("create_index_song", entity, message -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                byte[] bytes = SerializationUtils.serialize(authentication);
                String auth = DatatypeConverter.printBase64Binary(bytes);
                message.setStringProperty("authentication", auth);
            }
            return message;
        });
    }

    @Override
    public void delete(Song entity) {
        jmsTemplate.convertAndSend("delete_index_song", entity.getId(), message -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                byte[] bytes = SerializationUtils.serialize(authentication);
                String auth = DatatypeConverter.printBase64Binary(bytes);
                message.setStringProperty("authentication", auth);
            }
            return message;
        });
    }
}
