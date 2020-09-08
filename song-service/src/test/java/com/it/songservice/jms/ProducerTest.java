package com.it.songservice.jms;

import com.it.songservice.model.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class ProducerTest {

    @Autowired
    private Producer producer;

    @MockBean
    private Consumer consumer;

    @Test
    void upload() throws Exception {
        doNothing().when(consumer).upload(any());
        Resource resource = new Resource();
        resource.setName("test");
        producer.upload(resource);
    }
}