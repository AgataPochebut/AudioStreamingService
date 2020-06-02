package com.epam.conversionservice.jms;

import com.epam.conversionservice.service.ConversionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${conversion.defaultFolder}")
    private String defaultBaseFolder;

    @JmsListener(destination = "conversion")
//    @SendTo("conversion.out")
//    public Message listen(byte[] message, @Header("name") String name, @Header("format") String format) throws IOException {
    public void listen(BytesMessage message) throws JMSException, IOException {

        byte[] data = new byte[(int) message.getBodyLength()];
        message.readBytes(data);

        String name = message.getStringProperty("name");
        String format = message.getStringProperty("format");

        File file = new File(defaultBaseFolder, name);
        file.getParentFile().mkdirs();
        FileCopyUtils.copy(data, file);

        File newfile = conversionService.convert(file, format);

        jmsTemplate.send(message.getJMSReplyTo(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    BytesMessage bytesMessage = session.createBytesMessage();
                    InputStream fileInputStream = new FileInputStream(newfile);
                    final int BUFLEN = 64;
                    byte[] buf1 = new byte[BUFLEN];
                    int bytes_read = 0;
                    while ((bytes_read = fileInputStream.read(buf1)) != -1) {
                        bytesMessage.writeBytes(buf1, 0, bytes_read);
                    }
                    fileInputStream.close();

                    bytesMessage.setStringProperty("name", newfile.getName());

                    bytesMessage.setJMSCorrelationID(message.getJMSCorrelationID());

                    return bytesMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

}
