package com.epam.service.storage;

import com.epam.exception.FileStorageException;
import com.epam.exception.MyFileNotFoundException;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceFilesystem implements StorageService {

    @Override
    public File downloadFile(String path) {
        File file = new File(path);
        return file;
    }

    @Override
    public File uploadFile(MultipartFile file) throws IOException {

        String uploadsDir = "/uploads/";
        //String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
        if(! new File(uploadsDir).exists())
        {
            new File(uploadsDir).mkdir();
        }
        File newfile = new File(uploadsDir, file.getOriginalFilename());
        file.transferTo(newfile);

        return newfile;
    }
}
