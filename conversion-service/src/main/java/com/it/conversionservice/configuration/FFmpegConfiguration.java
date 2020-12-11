package com.it.conversionservice.configuration;

import net.bramp.ffmpeg.FFmpegExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("prod")
public class FFmpegConfiguration {

    @Bean
    public FFmpegExecutor executor() throws IOException {
        return new FFmpegExecutor();
    }

}
