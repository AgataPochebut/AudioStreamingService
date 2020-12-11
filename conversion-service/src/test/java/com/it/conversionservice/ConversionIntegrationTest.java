package com.it.conversionservice;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ConversionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void convertWAVtoMP3() throws Exception {
        Resource source = resourceLoader.getResource("classpath:hurts - stay.wav");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", source.getFilename(), "multipart/form-data", source.getInputStream());
        MvcResult mvcResult = this.mockMvc.perform(multipart("/conversion")
                .file(mockMultipartFile)
                .param("format", "mp3")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        org.springframework.core.io.Resource source1 = new ByteArrayResource(mvcResult.getResponse().getContentAsByteArray());
        assertThat(source).isNotNull();
        String name1 = ContentDisposition.parse(mvcResult.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION)).getFilename();
        assertThat(FilenameUtils.getExtension(name1).equalsIgnoreCase("mp3")).isTrue();
    }
}