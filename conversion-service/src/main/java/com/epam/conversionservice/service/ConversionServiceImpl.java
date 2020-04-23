package com.epam.conversionservice.service;

import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Value("${conversion.defaultFolder}")
    private String defaultBaseFolder;

    public File convert(File file, String format) throws IOException {
        if(FilenameUtils.getExtension(file.getName()).equals(format)) return file;

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

    @Override
    public Resource convert(Resource source, String format) throws IOException {
        if(FilenameUtils.getExtension(source.getFilename()).equals(format)) return source;

        File file;
        if (source.isFile()) {
            file = source.getFile();
        } else {
            file = new File(defaultBaseFolder, source.getFilename());
            file.getParentFile().mkdirs();
            FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));
        }

        return new FileSystemResource(convert(file, format));
    }
}
