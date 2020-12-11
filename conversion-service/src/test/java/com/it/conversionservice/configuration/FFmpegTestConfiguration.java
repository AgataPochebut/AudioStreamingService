package com.it.conversionservice.configuration;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("test")
public class FFmpegTestConfiguration {

    @Value("${FFMPEG}")
    String FFMPEG;
    @Value("${FFPROBE}")
    String FFPROBE;

    @Bean
    public FFmpegExecutor executor() throws IOException {
        return new FFmpegExecutor(new FFmpeg(FFMPEG), new FFprobe(FFPROBE));
    }

}
