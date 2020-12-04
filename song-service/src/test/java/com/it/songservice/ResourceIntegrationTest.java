package com.it.songservice;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.repository.ResourceRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.upload.ResourceUploadService;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
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
    private ResourceUploadService uploadService;

    @Autowired
    private ResourceRepositoryService repositoryService;

    @Autowired
    private ResourceStorageServiceManager storageService;

    @Qualifier("jmsConnectionFactory")
    @Autowired
    private ConnectionFactory connectionFactory;

    @BeforeEach
    void before() throws Exception {
        org.springframework.core.io.Resource source_test = new FileSystemResource("src/test/resources/hurts - stay.mp3");
        storageService.upload(source_test, source_test.getFilename());
    }

    @Test
    void uploadZip() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/Hurts - Exile.zip");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/resources")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        CountDownLatch latch = new CountDownLatch(1);

        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();
        Destination destination = session.createQueue("upload_zip");
        MessageConsumer consumer = session.createConsumer(destination);

        MessageListener listener = new MessageListener() {
            @SneakyThrows
            @Override
            public void onMessage(Message message) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Resource resource = (Resource) objectMessage.getObject();

                uploadService.setStatus(resource, UploadStatus.FINISHED);

                latch.countDown();
            }
        };

        consumer.setMessageListener(listener);
        connection.start();

        latch.await();

        session.close();
        connection.close();

        Long id = Long.valueOf(mvcResult.getResponse().getContentAsString());
        UploadStatus status = uploadService.getResultById(id).getStatus();
        assert(status).equals(UploadStatus.FINISHED);
    }

    @Test
    void uploadSong() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/HURTS - WONDERFUL LIFE.MP3");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/resources")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        CountDownLatch latch = new CountDownLatch(1);

        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();
        Destination destination = session.createQueue("upload_song");
        MessageConsumer consumer = session.createConsumer(destination);

        MessageListener listener = new MessageListener() {
            @SneakyThrows
            @Override
            public void onMessage(Message message) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Resource resource = (Resource) objectMessage.getObject();

                uploadService.setStatus(resource, UploadStatus.FINISHED);

                latch.countDown();
            }
        };

        consumer.setMessageListener(listener);
        connection.start();

        latch.await();

        session.close();
        connection.close();

        Long id = Long.valueOf(mvcResult.getResponse().getContentAsString());
        UploadStatus status = uploadService.getResultById(id).getStatus();
        assert(status).equals(UploadStatus.FINISHED);
    }

    @Test
    void download() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/resources/{id}", 1L)
                .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        org.springframework.core.io.Resource source = new ByteArrayResource(mvcResult.getResponse().getContentAsByteArray());
        assertThat(source).isNotNull();

        Resource entity = repositoryService.findById(1L);
        assertThat(source.contentLength()).isEqualTo(entity.getSize());
        assertThat(DigestUtils.md5Hex(source.getInputStream())).isEqualTo(entity.getChecksum());
    }

}
