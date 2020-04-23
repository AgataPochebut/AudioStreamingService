package com.epam.audiostreamingservice.service.repository;

import com.epam.audiostreamingservice.model.security.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {
}
