package com.epam.songservice.service;

import com.epam.songservice.model.Storage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageServiceImpl extends GenericServiceImpl<Storage, Long> implements StorageService {
}
