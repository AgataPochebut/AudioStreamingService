package com.epam.service.repository;

import com.epam.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {
}
