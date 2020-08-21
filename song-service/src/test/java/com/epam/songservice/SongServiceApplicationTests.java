package com.epam.songservice;

import com.epam.songservice.configuration.S3TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {S3TestConfiguration.class})
//@ContextConfiguration(classes = {S3TestConfiguration.class})
//@ImportAutoConfiguration(exclude = {S3Configuration.class})
//@Import(S3TestConfiguration.class)
class SongServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
