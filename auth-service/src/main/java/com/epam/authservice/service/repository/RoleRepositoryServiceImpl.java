package com.epam.authservice.service.repository;

import com.epam.authservice.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleRepositoryServiceImpl extends GenericRepositoryServiceImpl<Role, Long> implements RoleRepositoryService {
}
