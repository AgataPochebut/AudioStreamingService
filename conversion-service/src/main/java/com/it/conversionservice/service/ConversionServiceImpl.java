package com.it.conversionservice.service;

import com.it.conversionservice.exception.IncorrectFormatException;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Value("${fs.tempFolder}")
    private String defaultBaseFolder;

    public File convert(File file, String format) throws Exception {
        if(FilenameUtils.getExtension(file.getName()).equals(format)) throw new IncorrectFormatException("The same format");

        File newfile = new File(defaultBaseFolder, FilenameUtils.removeExtension(file.getName()) + "." + format);
        newfile.getParentFile().mkdirs();

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(file.getAbsolutePath())
                .overrideOutputFiles(true)

                .addOutput(newfile.getAbsolutePath())
                .done();
        new FFmpegExecutor().createJob(builder).run();

        return newfile;
    }


}
