//package com.epam.songservice.service;
//
//import com.epam.songservice.model.Resource;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//@Service
//@Transactional
//public class StorageService {
//
//    @Value("${fs.defaultFolder}")
//    private String defaultBaseFolder;
//
//    public Resource upload(org.springframework.core.io.Resource source, String name) throws IOException {
//        int count = 0;
//        File file = new File(defaultBaseFolder, name);
////        while (file.exists()){
////            count++;
////            file = new File(defaultBaseFolder, FilenameUtils.removeExtension(name) + " ("+ count + ")" + "." + FilenameUtils.getExtension(name));
////        }
//        file.getParentFile().mkdirs();
//
//        OutputStream output = new FileOutputStream(file);
//        IOUtils.copy(source.getInputStream(), output);
//        output.close();
//
//        throw new RuntimeException("hj");
//
////        FSResource resource = new FSResource();
////        resource.setName(file.getName());
////        resource.setSize(file.length());
////        resource.setChecksum(DigestUtils.md5Hex(source.getInputStream()));
////        resource.setPath(file.getAbsolutePath());
////        return resource;
//    }
//}
