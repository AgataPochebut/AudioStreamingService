package com.epam.service;

import com.epam.model.Artist;
import com.epam.model.Storage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageServiceImpl extends GenericServiceImpl<Storage, Long> implements StorageService {
}
