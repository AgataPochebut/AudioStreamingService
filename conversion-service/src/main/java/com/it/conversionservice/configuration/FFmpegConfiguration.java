package com.it.conversionservice.configuration;

import net.bramp.ffmpeg.FFmpegExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FFmpegConfiguration {

    @Bean
    public FFmpegExecutor executor() throws IOException {
        return new FFmpegExecutor();
    }

}
