package com.epam.service.repository;

import com.epam.model.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceServiceImpl extends GenericServiceImpl<Resource, Long> implements ResourceService {
}
