package com.epam.userservice.service;

import com.epam.userservice.model.Role;
import com.epam.userservice.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {
}
