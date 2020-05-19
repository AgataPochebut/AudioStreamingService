package com.epam.authservice.service;

import com.epam.authservice.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {
}
