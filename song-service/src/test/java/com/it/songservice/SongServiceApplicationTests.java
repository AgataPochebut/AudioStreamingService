package com.it.songservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.Enumeration;

@SpringBootTest
class SongServiceApplicationTests {

    @Test
    void contextLoads() {

    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    void queueTest() throws JMSException {

        String dest = "status1";

        jmsTemplate.execute((session, producer) -> {
            Destination destination = session.createQueue(dest);

            String task = "Task";
            for (int i = 0; i < 10; i++) {
                String payload = task + i;
                Message msg = session.createObjectMessage(payload);
                msg.setJMSCorrelationID(String.valueOf(100));
                producer.send(destination, msg);
            }

            return null;
        });

        String selector = String.format("JMSCorrelationID='%s'", 100);

        jmsTemplate.execute((session) -> {
            Destination destination = session.createQueue(dest);
            QueueBrowser browser = session.createBrowser((Queue) destination, selector);
            Enumeration e = browser.getEnumeration();
            Message msg1;
            while ((msg1 = (Message) e.nextElement()) != null) {
                System.out.println(msg1);
            }
            browser.close();
            return null;
        }, true);
    }

}
