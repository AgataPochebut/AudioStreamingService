package com.it.songservice.service.upload;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadResult;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.repository.UploadResultRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class ZipUploadServiceImpl implements ZipUploadService {

    @Autowired
    ResourceUploadService resourceUploadService;

    @Autowired
    ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private UploadResultService uploadResultService;

    @Autowired
    private UploadResultRepositoryService uploadResultRepositoryService;

    public void upload(Resource resource) throws Exception {
        UploadResult result = uploadResultRepositoryService.findByResource(resource.getId());
        try {
            uploadResultService.setStatus(result, UploadStatus.PROCEEDED);
            org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
            ZipInputStream zin = new ZipInputStream(source.getInputStream());
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    UploadResult result1 = new UploadResult();
                    result1.setParent(result);
                    try {
                        byte[] content = IOUtils.toByteArray(zin);
                        org.springframework.core.io.Resource source1 = new ByteArrayResource(content);
                        String name1 = FilenameUtils.getName(entry.getName());
                        Resource resource1 = resourceStorageServiceManager.upload(source1, name1);
                        result1.setResource(resource1.getId());
                        result1.setStatus(UploadStatus.STORED);
                        result1 = uploadResultRepositoryService.save(result1);
                        resourceUploadService.upload(resource1);
                    } catch (Exception e) {
                        result1 = uploadResultRepositoryService.save(result1);

                        String errorMessage = e.getMessage();
                        log.error(errorMessage, e);

                        uploadResultService.setStatus(result1, UploadStatus.FAILED);
                        uploadResultService.setMess(result1, errorMessage);
                    }
                }
                zin.closeEntry();
            }
            zin.close();
            uploadResultService.setStatus(result, UploadStatus.UNPACKED);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);

            uploadResultService.setStatus(result, UploadStatus.FAILED);
            uploadResultService.setMess(result, errorMessage);
        } finally {
            try {
                resourceStorageServiceManager.delete(resource);
            } catch (Exception ex) {
            }
        }
    }
}
