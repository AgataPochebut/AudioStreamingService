package com.epam.service.conversion;

import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Value("${conversion.defaultFolder}")
    private String defaultBaseFolder;

    public File convert(File file, String format) throws IOException {
        if(FilenameUtils.getExtension(file.getName()).equals(format)) return file;

        File dir = new File(defaultBaseFolder);
        if (!dir.exists()) dir.mkdir();

        File newfile = new File(defaultBaseFolder, FilenameUtils.removeExtension(file.getName()) + "." + format);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(file.getAbsolutePath())
                .overrideOutputFiles(true)

                .addOutput(newfile.getAbsolutePath())
                .done();

        new FFmpegExecutor().createJob(builder).run();
        return newfile;
    }
}
