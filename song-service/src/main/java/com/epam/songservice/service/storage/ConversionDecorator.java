package com.epam.songservice.service.storage;

import com.epam.songservice.feign.conversion.ConversionClient;
import com.epam.songservice.model.Resource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ConversionDecorator extends ResourceStorageDecorator {

    private ConversionClient conversionService;

    public ConversionDecorator(ResourceStorageService storageService, ConversionClient conversionService) {
        super(storageService);
        this.conversionService = conversionService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {
        if (FilenameUtils.getExtension(source.getFilename()).equals("wav")) {
            //todo
            FileItem fileItem = new DiskFileItemFactory().createItem("file",
                    "text/plain", false, source.getFilename());

//            new DiskFileItem(
//                    "data",
//                    "text/plain",
//                    false,
//                    source.getFilename(),
//                    (int) source.contentLength(),
//                    file.getParentFile());

            source = conversionService.convert(new CommonsMultipartFile(fileItem), "mp3").getBody();
        }
        return super.upload(source);
    }

    @Override
    public String test() {
        return super.test() + " Conversion";
    }

}
