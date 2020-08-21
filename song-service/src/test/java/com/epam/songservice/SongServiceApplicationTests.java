package com.epam.songservice;

import com.epam.songservice.configuration.S3Configuration;
import com.epam.songservice.configuration.S3TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@ImportAutoConfiguration(exclude = {S3Configuration.class})
@Import(S3TestConfiguration.class)
//@ContextConfiguration(classes = {S3TestConfiguration.class, MappingConfiguration.class})
class SongServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
