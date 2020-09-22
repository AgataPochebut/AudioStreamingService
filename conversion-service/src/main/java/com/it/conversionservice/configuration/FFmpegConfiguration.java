package com.it.conversionservice.configuration;

import net.bramp.ffmpeg.FFmpegExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FFmpegConfiguration {

//    @Value("${ffmpeg}")
//    String ffmpeg;
//    @Value("${ffprobe}")
//    String ffprobe;

    @Bean
    public FFmpegExecutor executor() throws IOException {
        return new FFmpegExecutor();//new FFmpeg(ffmpeg), new FFprobe(ffprobe));
    }

}
