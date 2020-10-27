package com.it.songservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("storage.type=S3")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void upload() throws Exception {
        org.springframework.core.io.Resource source = new FileSystemResource("src/test/resources/Hurts - Exile.zip");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        this.mockMvc.perform(multipart("/resources")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }

}
