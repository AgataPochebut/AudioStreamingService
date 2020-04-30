package com.epam.songservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JmsTemplate jmsTemplate;

//    @PostMapping("/1")
//    public void test(@RequestParam("data") MultipartFile multipartFile) throws IOException {
//        ConversionService service = context.getBean(ConversionService.class);
//        Resource resource = service.convert(multipartFile.getResource(), "mp3");
//    }
//
//    @PostMapping("/2")
//    public String test(@RequestParam("name") String name) throws IOException {
//        ConversionService service = context.getBean(ConversionService.class);
//        String result = service.test(name);
//        return result;
//    }

    @PostMapping("/3")
    public void test3(@RequestParam("data") MultipartFile multipartFile) throws IOException {

        jmsTemplate.send("conversion.in", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    BytesMessage bytesMessage = session.createBytesMessage();
                    InputStream fileInputStream = multipartFile.getInputStream();
                    final int BUFLEN = 64;
                    byte[] buf1 = new byte[BUFLEN];
                    int bytes_read = 0;
                    while ((bytes_read = fileInputStream.read(buf1)) != -1) {
                        bytesMessage.writeBytes(buf1, 0, bytes_read);
                    }
                    fileInputStream.close();

                    bytesMessage.setStringProperty("name", multipartFile.getOriginalFilename());
                    bytesMessage.setStringProperty("format", "mp3");
                    return bytesMessage;
                } catch (Exception e) {
                    return null;
                }

            }
        });
    }

    @PostMapping(value = "/4", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> test4(@RequestParam("data") MultipartFile multipartFile) throws IOException, JMSException {

        Resource source = multipartFile.getResource();
        String name = multipartFile.getOriginalFilename();

        BytesMessage message = (BytesMessage) jmsTemplate.sendAndReceive("conversion.in", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    BytesMessage bytesMessage = session.createBytesMessage();
                    InputStream fileInputStream = source.getInputStream();
                    final int BUFLEN = 64;
                    byte[] buf1 = new byte[BUFLEN];
                    int bytes_read = 0;
                    while ((bytes_read = fileInputStream.read(buf1)) != -1) {
                        bytesMessage.writeBytes(buf1, 0, bytes_read);
                    }
                    fileInputStream.close();

                    bytesMessage.setStringProperty("name", name);
                    bytesMessage.setStringProperty("format", "mp3");

                    bytesMessage.setJMSCorrelationID("123");

                    return bytesMessage;
                } catch (Exception e) {
                    return null;
                }

            }
        });

        byte[] data = new byte[(int) message.getBodyLength()];
        message.readBytes(data);

        Resource newsource = new ByteArrayResource(data);
        String newname = message.getStringProperty("name");

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(newname)
                .build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(newsource, headers, HttpStatus.OK);

    }

    @PostMapping("/5")
    public void test5(@RequestParam("data") MultipartFile multipartFile) throws IOException {

        jmsTemplate.convertAndSend("conversion.in", multipartFile.getBytes(), new MessagePostProcessor() {
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("name", multipartFile.getOriginalFilename());
                message.setStringProperty("format", "mp3");
                return message;
            }
        });
    }

}
