package com.it.songservice;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.repository.ResourceRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.upload.ResourceUploadService;
import com.it.songservice.service.upload.SongUploadService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("storage.type=S3")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private ResourceUploadService uploadService;

    @Autowired
    private SongUploadService songUploadService;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Qualifier("jmsConnectionFactory")
    @Autowired
    private ConnectionFactory connectionFactory;

    @BeforeEach
    void before() throws Exception {
        org.springframework.core.io.Resource source = resourceLoader.getResource("classpath:hurts - stay.mp3");
        resourceStorageServiceManager.upload(source, source.getFilename());
    }

    @Test
    void uploadZip() throws Exception {
        org.springframework.core.io.Resource source = resourceLoader.getResource("classpath:Hurts - Exile.zip");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/resources")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        Long id = Long.valueOf(mvcResult.getResponse().getContentAsString());

        CountDownLatch latch = new CountDownLatch(1);

        String selector = String.format("JMSCorrelationID='%s'", id);
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();
        Destination destination = session.createQueue("status");
        MessageConsumer consumer = session.createConsumer(destination, selector);
        MessageListener listener = message -> {
            try {
                UploadStatus status = message.getBody(UploadStatus.class);
                if (status == UploadStatus.FINISHED) latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        consumer.setMessageListener(listener);
        connection.start();

        latch.await();

        session.close();
        connection.close();

//        UploadStatus status = uploadService.getResultById(id).getStatus();
//        assert(status).equals(UploadStatus.FINISHED);
    }

    @Test
    void uploadSong() throws Exception {
        org.springframework.core.io.Resource source = resourceLoader.getResource("classpath:HURTS - WONDERFUL LIFE.MP3");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/resources")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        Long id = Long.valueOf(mvcResult.getResponse().getContentAsString());

        CountDownLatch latch = new CountDownLatch(1);

        String selector = String.format("JMSCorrelationID='%s'", id);
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();
        Destination destination = session.createQueue("upload_song");
        MessageConsumer consumer = session.createConsumer(destination, selector);
        MessageListener listener = new MessageListener() {
            @Override
            public void onMessage(Message message) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                try {
                    Resource resource = (Resource) objectMessage.getObject();
                    songUploadService.upload(resource);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        };

        consumer.setMessageListener(listener);
        connection.start();

        latch.await();

        session.close();
        connection.close();

//        UploadStatus status = uploadService.getResultById(id).getStatus();
//        assert(status).equals(UploadStatus.FINISHED);
    }

    @Test
    void download() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/resources/{id}", 1L)
                .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        org.springframework.core.io.Resource source = new ByteArrayResource(mvcResult.getResponse().getContentAsByteArray());
        Resource entity = repositoryService.findById(1L);
        assertThat(source).isNotNull();
        assertThat(source.contentLength()).isEqualTo(entity.getSize());
        assertThat(DigestUtils.md5Hex(source.getInputStream())).isEqualTo(entity.getChecksum());
    }

}
