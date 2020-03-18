package com.epam.userservice.component;

import com.epam.userservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    @JmsListener(destination = "test-queue")
    public void listener(User message){

        log.info("Message received {} ", message);
    }
}
