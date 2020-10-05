package com.it.conversionservice.service;

import com.it.conversionservice.exception.IncorrectFormatException;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Value("${fs.tempFolder}")
    private String defaultBaseFolder;

    @Autowired
    private FFmpegExecutor executor;

    public File convert(File file, String format) throws Exception {
        if(FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(format)) throw new IncorrectFormatException("The same format");

        String newname = FilenameUtils.removeExtension(file.getName()) + "." + format;
        File newfile = new File(defaultBaseFolder, newname);
        newfile.getParentFile().mkdirs();

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(file.getAbsolutePath())
                .overrideOutputFiles(true)

                .addOutput(newfile.getAbsolutePath())
                .done();
        executor.createJob(builder).run();

        return newfile;
    }


}
